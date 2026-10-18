package com.shop.controller;

import com.shop.api.OrdersApi;
import com.shop.model.OrderCreateRequest;
import com.shop.model.OrderPageResponse;
import com.shop.model.OrderResponse;
import com.shop.model.OrderStatusUpdateRequest;
import com.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class OrdersController implements OrdersApi {

    private final OrderService orderService;

    @Override
    public ResponseEntity<OrderResponse> createOrder(OrderCreateRequest orderCreateRequest) {
        OrderResponse created = orderService.createOrder(orderCreateRequest);
        return ResponseEntity.created(URI.create("/api/v1/orders/" + created.getId())).body(created);
    }

    @Override
    public ResponseEntity<OrderResponse> getOrderById(Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @Override
    public ResponseEntity<OrderResponse> updateOrderStatus(Long id, OrderStatusUpdateRequest orderStatusUpdateRequest) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, orderStatusUpdateRequest));
    }

    @GetMapping("/orders")
    public ResponseEntity<OrderPageResponse> getOrders(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(orderService.getOrders(page, size));
    }
}
