package com.example.fooddelivery.food_delivery_backend.service;

import com.example.fooddelivery.food_delivery_backend.dto.*;
import com.example.fooddelivery.food_delivery_backend.entity.Order;
import com.example.fooddelivery.food_delivery_backend.entity.OrderStatus;
import com.example.fooddelivery.food_delivery_backend.exception.OrderNotFoundException;
import com.example.fooddelivery.food_delivery_backend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final QueueService queueService;

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequest) {
        Order order = Order.builder()
                .customerName(orderRequest.getCustomerName())
                .items(orderRequest.getItems())
                .totalAmount(orderRequest.getTotalAmount())
                .orderTime(orderRequest.getOrderTime())
                .orderStatus(OrderStatus.PLACED)
                .build();

        Order savedOrder = orderRepository.save(order);
        queueService.sendOrderForProcessing(savedOrder.getId());
        return mapToResponseDTO(savedOrder);
    }

    public Page<OrderResponseDTO> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(this::mapToResponseDTO);
    }

    public OrderResponseDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));
        return mapToResponseDTO(order);
    }

    public OrderStatusDTO getOrderStatus(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));
        return OrderStatusDTO.builder()
            .orderId(orderId)
            .orderStatus(order.getOrderStatus().name())
            .message("Order status retrieved successfully")
            .build();
    }

    @Transactional
    public OrderStatusDTO updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

        try {
            OrderStatus newStatus = OrderStatus.valueOf(status.toUpperCase());
            order.setOrderStatus(newStatus);
            orderRepository.save(order);

            return OrderStatusDTO.builder()
                .orderId(orderId)
                .orderStatus(newStatus.name())
                .message("Order status updated successfully")
                .build();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid order status: " + status);
        }
    }

    private OrderResponseDTO mapToResponseDTO(Order order) {
        return OrderResponseDTO.builder()
                .id(order.getId())
                .customerName(order.getCustomerName())
                .items(order.getItems())
                .totalAmount(order.getTotalAmount())
                .orderTime(order.getOrderTime())
                .orderStatus(order.getOrderStatus().name())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
