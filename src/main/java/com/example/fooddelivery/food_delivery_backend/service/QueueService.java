package com.example.fooddelivery.food_delivery_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueueService {

    @Value("${queue.in-memory.enabled:true}")
    private boolean inMemoryQueueEnabled;

    private final BlockingQueue<Long> orderQueue = new LinkedBlockingQueue<>();

    private final OrderProcessingService orderProcessingService;

    public void sendOrderForProcessing(Long orderId) {
        if (inMemoryQueueEnabled) {
            sendToInMemoryQueue(orderId);
        } else {
            sendToSQS(orderId); // Placeholder for actual SQS implementation
        }
    }

    private void sendToInMemoryQueue(Long orderId) {
        try {
            orderQueue.put(orderId);
            log.info("Order {} sent to in-memory queue", orderId);
            processOrderFromQueue();
        } catch (InterruptedException e) {
            log.error("Error sending order to queue: {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private void sendToSQS(Long orderId) {
        log.info("Order {} sent to SQS queue (not implemented here)", orderId);
        // Add AWS SQS send message logic here if needed
    }

    private void processOrderFromQueue() {
        try {
            Long orderId = orderQueue.take();
            orderProcessingService.processOrderAsync(orderId);
        } catch (InterruptedException e) {
            log.error("Error processing order from queue: {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
