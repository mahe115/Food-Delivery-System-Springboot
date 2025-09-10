package com.example.fooddelivery.food_delivery_backend.service;

import com.example.fooddelivery.food_delivery_backend.entity.Order;
import com.example.fooddelivery.food_delivery_backend.entity.OrderStatus;
import com.example.fooddelivery.food_delivery_backend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderProcessingService {

    private final OrderRepository orderRepository;

    @Value("${queue.processing.delay:5000}")
    private long processingDelay;

    @Async
    public void processOrderAsync(Long orderId) {
        log.info("Starting async processing for order: {}", orderId);

        try {
            Thread.sleep(processingDelay);

            Order order = orderRepository.findById(orderId).orElse(null);
            if (order != null) {
                order.setOrderStatus(OrderStatus.PROCESSED);
                orderRepository.save(order);
                log.info("Order {} processed successfully", orderId);
            }
        } catch (InterruptedException e) {
            log.error("Order processing interrupted: {}", e.getMessage());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("Error processing order {}: {}", orderId, e.getMessage());
        }
    }
}
