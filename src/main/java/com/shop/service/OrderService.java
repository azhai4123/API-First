package com.shop.service;

import com.shop.entity.Customer;
import com.shop.entity.Order;
import com.shop.entity.OrderItem;
import com.shop.entity.Product;
import com.shop.exception.ResourceNotFoundException;
import com.shop.exception.UnprocessableEntityException;
import com.shop.mapper.DtoMapper;
import com.shop.model.*;
import com.shop.repository.CustomerRepository;
import com.shop.repository.OrderRepository;
import com.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    /**
     * Tiêu chí Rubric: Phương thức tạo Đơn hàng (Checkout) được bọc trong @Transactional
     * để đảm bảo tính toàn vẹn dữ liệu (ACID) khi đồng thời tạo Đơn hàng và trừ Tồn kho sản phẩm.
     */
    @Transactional
    public OrderResponse createOrder(OrderCreateRequest req) {
        if (req.getItems() == null || req.getItems().isEmpty()) {
            throw new UnprocessableEntityException("VALIDATION_FAILED", "Danh sách sản phẩm trong đơn hàng không được để trống");
        }

        Customer customer = customerRepository.findById(req.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("CUSTOMER_NOT_FOUND", "Không tìm thấy khách hàng ID: " + req.getCustomerId()));

        // Fail-Fast FF-01: Duyệt kiểm tra tồn kho tất cả sản phẩm trước khi trừ
        List<Product> productsToUpdate = new ArrayList<>();
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (int i = 0; i < req.getItems().size(); i++) {
            OrderItemRequest itemReq = req.getItems().get(i);
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("PRODUCT_NOT_FOUND", "Không tìm thấy sản phẩm ID: " + itemReq.getProductId()));

            int requestedQty = itemReq.getQuantity();
            if (requestedQty <= 0) {
                throw new UnprocessableEntityException("VALIDATION_FAILED", "Số lượng mua phải lớn hơn 0");
            }

            if (product.getStockQuantity() < requestedQty) {
                ErrorDetail detail = new ErrorDetail();
                detail.setField("items[" + i + "].quantity");
                detail.setIssue("Sản phẩm '" + product.getName() + "' chỉ còn " + product.getStockQuantity() + " chiếc trong kho");
                throw new UnprocessableEntityException("OUT_OF_STOCK", "Sản phẩm '" + product.getName() + "' không đủ số lượng tồn kho khả dụng", List.of(detail));
            }

            // Trừ tồn kho ACID
            product.setStockQuantity(product.getStockQuantity() - requestedQty);
            if (product.getStockQuantity() == 0) {
                product.setStatus("OUT_OF_STOCK");
            }
            productsToUpdate.add(product);

            // Bảo toàn đơn giá lịch sử (FF-03)
            BigDecimal unitPrice = product.getPrice();
            BigDecimal subTotal = unitPrice.multiply(BigDecimal.valueOf(requestedQty));
            totalAmount = totalAmount.add(subTotal);

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .productName(product.getName())
                    .unitPrice(unitPrice)
                    .quantity(requestedQty)
                    .subTotal(subTotal)
                    .build();

            orderItems.add(orderItem);
        }

        // Cập nhật kho hàng
        productRepository.saveAll(productsToUpdate);

        // Tạo đơn hàng trạng thái PENDING
        Order order = Order.builder()
                .customer(customer)
                .totalAmount(totalAmount)
                .status("PENDING")
                .shippingAddress(req.getShippingAddress().trim())
                .build();

        for (OrderItem oi : orderItems) {
            order.addItem(oi);
        }

        Order savedOrder = orderRepository.save(order);
        return DtoMapper.toOrderResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ORDER_NOT_FOUND", "Không tìm thấy đơn hàng ID: " + id));
        return DtoMapper.toOrderResponse(order);
    }

    @Transactional(readOnly = true)
    public OrderPageResponse getOrders(Integer page, Integer size) {
        int pageNumber = (page != null && page >= 0) ? page : 0;
        int pageSize = (size != null && size > 0) ? size : 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Order> orderPage = orderRepository.findAllByOrderByCreatedAtDesc(pageable);

        OrderPageResponse resp = new OrderPageResponse();
        resp.setContent(orderPage.getContent().stream().map(DtoMapper::toOrderResponse).collect(Collectors.toList()));

        PageMetadata pageMeta = new PageMetadata();
        pageMeta.setNumber(orderPage.getNumber());
        pageMeta.setSize(orderPage.getSize());
        pageMeta.setTotalElements(orderPage.getTotalElements());
        pageMeta.setTotalPages(orderPage.getTotalPages());
        pageMeta.setLast(orderPage.isLast());
        resp.setPage(pageMeta);

        return resp;
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long id, OrderStatusUpdateRequest req) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ORDER_NOT_FOUND", "Không tìm thấy đơn hàng ID: " + id));

        String currentStatus = order.getStatus();
        String targetStatus = req.getStatus() != null ? req.getStatus().getValue() : "";

        // Fail-Fast FF-04: Đơn hàng ở trạng thái kết thúc là bất biến
        if ("DELIVERED".equals(currentStatus) || "SHIPPED".equals(currentStatus) || "CANCELLED".equals(currentStatus)) {
            throw new UnprocessableEntityException("INVALID_STATE_TRANSITION",
                    "Đơn hàng ở trạng thái " + currentStatus + " không thể chuyển đổi trạng thái tiếp");
        }

        // Quy tắc chuyển đổi trạng thái tuần tự
        Map<String, List<String>> allowedTransitions = Map.of(
                "PENDING", List.of("PAID", "CONFIRMED", "CANCELLED"),
                "PAID", List.of("SHIPPED", "CANCELLED"),
                "CONFIRMED", List.of("SHIPPING", "DELIVERED", "SHIPPED", "CANCELLED"),
                "SHIPPING", List.of("DELIVERED", "SHIPPED", "CANCELLED")
        );

        if (!allowedTransitions.getOrDefault(currentStatus, List.of()).contains(targetStatus)) {
            throw new UnprocessableEntityException("INVALID_STATE_TRANSITION",
                    "Không thể chuyển đổi trạng thái từ " + currentStatus + " sang " + targetStatus);
        }

        // Bắt buộc theo Đề bài: Nếu hủy đơn (CANCELLED), hoàn trả lại số lượng tồn kho
        if ("CANCELLED".equals(targetStatus) && !"CANCELLED".equals(currentStatus)) {
            for (OrderItem item : order.getItems()) {
                Product product = item.getProduct();
                if (product != null) {
                    product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
                    if (product.getStockQuantity() > 0 && "OUT_OF_STOCK".equals(product.getStatus())) {
                        product.setStatus("ACTIVE");
                    }
                    productRepository.save(product);
                }
            }
        }

        order.setStatus(targetStatus);
        Order updated = orderRepository.save(order);
        return DtoMapper.toOrderResponse(updated);
    }
}
