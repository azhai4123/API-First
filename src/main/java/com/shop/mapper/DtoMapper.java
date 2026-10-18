package com.shop.mapper;

import com.shop.entity.Category;
import com.shop.entity.Customer;
import com.shop.entity.Order;
import com.shop.entity.OrderItem;
import com.shop.entity.Product;
import com.shop.model.*;

import java.net.URI;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class DtoMapper {

    public static OffsetDateTime toOffsetDateTime(Instant instant) {
        return instant != null ? instant.atOffset(ZoneOffset.UTC) : null;
    }

    private static URI toUri(String uriString) {
        if (uriString == null || uriString.isBlank()) return null;
        try {
            return URI.create(uriString.trim());
        } catch (Exception e) {
            return null;
        }
    }

    public static CategoryResponse toCategoryResponse(Category category) {
        if (category == null) return null;
        CategoryResponse resp = new CategoryResponse();
        resp.setId(category.getId());
        resp.setCode(category.getCode());
        resp.setName(category.getName());
        resp.setDescription(category.getDescription());
        if (category.getStatus() != null) {
            try {
                resp.setStatus(CategoryResponse.StatusEnum.fromValue(category.getStatus()));
            } catch (Exception ignored) {}
        }
        resp.setCreatedAt(toOffsetDateTime(category.getCreatedAt()));
        resp.setUpdatedAt(toOffsetDateTime(category.getUpdatedAt()));
        return resp;
    }

    public static ProductResponse toProductResponse(Product product) {
        if (product == null) return null;
        ProductResponse resp = new ProductResponse();
        resp.setId(product.getId());
        resp.setName(product.getName());
        resp.setSku(product.getSku());
        resp.setPrice(product.getPrice() != null ? product.getPrice().doubleValue() : 0.0);
        resp.setStockQuantity(product.getStockQuantity());
        resp.setImageUrl(toUri(product.getImageUrl()));
        resp.setDescription(product.getDescription());
        if (product.getCategory() != null) {
            resp.setCategoryId(product.getCategory().getId());
            resp.setCategory(product.getCategory().getName());
        }
        if (product.getStatus() != null) {
            try {
                resp.setStatus(ProductResponse.StatusEnum.fromValue(product.getStatus()));
            } catch (Exception ignored) {}
        }
        resp.setCreatedAt(toOffsetDateTime(product.getCreatedAt()));
        resp.setUpdatedAt(toOffsetDateTime(product.getUpdatedAt()));
        return resp;
    }

    public static CustomerResponse toCustomerResponse(Customer customer) {
        if (customer == null) return null;
        CustomerResponse resp = new CustomerResponse();
        resp.setId(customer.getId());
        resp.setEmail(customer.getEmail());
        resp.setFullName(customer.getFullName());
        resp.setPhone(customer.getPhone());
        resp.setAddress(customer.getAddress());
        resp.setAvatarUrl(toUri(customer.getAvatarUrl()));
        if (customer.getRole() != null) {
            try {
                resp.setRole(CustomerResponse.RoleEnum.fromValue(customer.getRole()));
            } catch (Exception ignored) {}
        }
        if (customer.getStatus() != null) {
            try {
                resp.setStatus(CustomerResponse.StatusEnum.fromValue(customer.getStatus()));
            } catch (Exception ignored) {}
        }
        resp.setCreatedAt(toOffsetDateTime(customer.getCreatedAt()));
        resp.setUpdatedAt(toOffsetDateTime(customer.getUpdatedAt()));
        return resp;
    }

    public static OrderItemResponse toOrderItemResponse(OrderItem item) {
        if (item == null) return null;
        OrderItemResponse resp = new OrderItemResponse();
        resp.setId(item.getId());
        resp.setProductId(item.getProduct() != null ? item.getProduct().getId() : null);
        resp.setProductName(item.getProductName());
        resp.setUnitPrice(item.getUnitPrice() != null ? item.getUnitPrice().doubleValue() : 0.0);
        resp.setQuantity(item.getQuantity());
        resp.setSubtotal(item.getSubTotal() != null ? item.getSubTotal().doubleValue() : 0.0);
        resp.setCreatedAt(toOffsetDateTime(item.getCreatedAt()));
        return resp;
    }

    public static OrderResponse toOrderResponse(Order order) {
        if (order == null) return null;
        OrderResponse resp = new OrderResponse();
        resp.setId(order.getId());
        resp.setCustomerId(order.getCustomer() != null ? order.getCustomer().getId() : null);
        resp.setTotalAmount(order.getTotalAmount() != null ? order.getTotalAmount().doubleValue() : 0.0);
        if (order.getStatus() != null) {
            try {
                resp.setStatus(OrderResponse.StatusEnum.fromValue(order.getStatus()));
            } catch (Exception ignored) {}
        }
        resp.setShippingAddress(order.getShippingAddress());
        if (order.getItems() != null) {
            resp.setItems(order.getItems().stream()
                    .map(DtoMapper::toOrderItemResponse)
                    .collect(Collectors.toList()));
        } else {
            resp.setItems(new ArrayList<>());
        }
        resp.setCreatedAt(toOffsetDateTime(order.getCreatedAt()));
        resp.setUpdatedAt(toOffsetDateTime(order.getUpdatedAt()));
        return resp;
    }
}
