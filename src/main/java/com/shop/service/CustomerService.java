package com.shop.service;

import com.shop.entity.Customer;
import com.shop.entity.Order;
import com.shop.exception.ConflictException;
import com.shop.exception.ResourceNotFoundException;
import com.shop.mapper.DtoMapper;
import com.shop.model.*;
import com.shop.repository.CustomerRepository;
import com.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public CustomerPageResponse getCustomers(Integer page, Integer size, String sort) {
        int pageNumber = (page != null && page > 0) ? page - 1 : 0;
        int pageSize = (size != null && size > 0) ? size : 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Customer> custPage = customerRepository.findAll(pageable);

        CustomerPageResponse resp = new CustomerPageResponse();
        resp.setContent(custPage.getContent().stream().map(DtoMapper::toCustomerResponse).collect(Collectors.toList()));

        PageMetadata pageMeta = new PageMetadata();
        pageMeta.setNumber(custPage.getNumber() + 1);
        pageMeta.setSize(custPage.getSize());
        pageMeta.setTotalElements(custPage.getTotalElements());
        pageMeta.setTotalPages(custPage.getTotalPages());
        pageMeta.setLast(custPage.isLast());
        resp.setPage(pageMeta);

        return resp;
    }

    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CUSTOMER_NOT_FOUND", "Không tìm thấy khách hàng ID: " + id));
        return DtoMapper.toCustomerResponse(customer);
    }

    @Transactional(readOnly = true)
    public OrderPageResponse getCustomerOrders(Long id, Integer page, Integer size) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("CUSTOMER_NOT_FOUND", "Không tìm thấy khách hàng ID: " + id);
        }

        List<Order> orders = orderRepository.findByCustomerIdOrderByCreatedAtDesc(id);

        OrderPageResponse resp = new OrderPageResponse();
        resp.setContent(orders.stream().map(DtoMapper::toOrderResponse).collect(Collectors.toList()));

        PageMetadata pageMeta = new PageMetadata();
        pageMeta.setNumber(0);
        pageMeta.setSize(orders.size() > 0 ? orders.size() : 10);
        pageMeta.setTotalElements((long) orders.size());
        pageMeta.setTotalPages(1);
        pageMeta.setLast(true);
        resp.setPage(pageMeta);

        return resp;
    }

    @Transactional
    public CustomerResponse createCustomer(CustomerCreateRequest req) {
        if (customerRepository.existsByEmail(req.getEmail())) {
            throw new ConflictException("EMAIL_ALREADY_EXISTS", "Email '" + req.getEmail() + "' đã được đăng ký");
        }

        Customer customer = Customer.builder()
                .email(req.getEmail().trim().toLowerCase())
                .password(passwordEncoder.encode("123456"))
                .fullName(req.getFullName().trim())
                .phone(req.getPhone() != null ? req.getPhone().trim() : "")
                .address(req.getAddress() != null ? req.getAddress().trim() : "")
                .avatarUrl(req.getAvatarUrl() != null ? req.getAvatarUrl().toString() : "")
                .role("CUSTOMER")
                .status("ACTIVE")
                .build();

        Customer saved = customerRepository.save(customer);
        return DtoMapper.toCustomerResponse(saved);
    }

    @Transactional
    public CustomerResponse updateCustomer(Long id, CustomerUpdateRequest req) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CUSTOMER_NOT_FOUND", "Không tìm thấy khách hàng ID: " + id));

        if (req.getFullName() != null && !req.getFullName().isBlank()) customer.setFullName(req.getFullName().trim());
        if (req.getPhone() != null && !req.getPhone().isBlank()) customer.setPhone(req.getPhone().trim());
        if (req.getAddress() != null && !req.getAddress().isBlank()) customer.setAddress(req.getAddress().trim());
        if (req.getStatus() != null) customer.setStatus(req.getStatus().getValue());

        Customer updated = customerRepository.save(customer);
        return DtoMapper.toCustomerResponse(updated);
    }

    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CUSTOMER_NOT_FOUND", "Không tìm thấy khách hàng ID: " + id));
        customerRepository.delete(customer);
    }
}
