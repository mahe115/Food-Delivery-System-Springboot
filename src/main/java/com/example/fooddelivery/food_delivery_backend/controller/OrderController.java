package com.example.fooddelivery.food_delivery_backend.controller;

import com.example.fooddelivery.food_delivery_backend.dto.*;
import com.example.fooddelivery.food_delivery_backend.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO orderRequest) {
        OrderResponseDTO response = orderService.createOrder(orderRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponseDTO>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "orderTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<OrderResponseDTO> orders = orderService.getAllOrders(pageable);

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long orderId) {
        OrderResponseDTO response = orderService.getOrderById(orderId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}/status")
    public ResponseEntity<OrderStatusDTO> getOrderStatus(@PathVariable Long orderId) {
        OrderStatusDTO response = orderService.getOrderStatus(orderId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderStatusDTO> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String status) {
        OrderStatusDTO response = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(response);
    }
}
