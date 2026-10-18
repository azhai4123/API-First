package com.shop.controller;

import com.shop.api.CustomersApi;
import com.shop.model.CustomerCreateRequest;
import com.shop.model.CustomerPageResponse;
import com.shop.model.CustomerResponse;
import com.shop.model.CustomerUpdateRequest;
import com.shop.model.OrderPageResponse;
import com.shop.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class CustomersController implements CustomersApi {

    private final CustomerService customerService;

    @Override
    public ResponseEntity<CustomerResponse> createCustomer(CustomerCreateRequest customerCreateRequest) {
        CustomerResponse created = customerService.createCustomer(customerCreateRequest);
        return ResponseEntity.created(URI.create("/api/v1/customers/" + created.getId())).body(created);
    }

    @Override
    public ResponseEntity<Void> deleteCustomer(Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<CustomerResponse> getCustomerById(Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @Override
    public ResponseEntity<OrderPageResponse> getCustomerOrders(Long id, Integer page, Integer size) {
        return ResponseEntity.ok(customerService.getCustomerOrders(id, page, size));
    }

    @Override
    public ResponseEntity<CustomerPageResponse> getCustomers(Integer page, Integer size, String sort) {
        return ResponseEntity.ok(customerService.getCustomers(page, size, sort));
    }

    @Override
    public ResponseEntity<CustomerResponse> updateCustomer(Long id, CustomerUpdateRequest customerUpdateRequest) {
        return ResponseEntity.ok(customerService.updateCustomer(id, customerUpdateRequest));
    }
}
