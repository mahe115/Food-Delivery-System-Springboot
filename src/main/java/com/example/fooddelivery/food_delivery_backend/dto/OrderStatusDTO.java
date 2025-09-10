package com.example.fooddelivery.food_delivery_backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderStatusDTO {
    private Long orderId;
    private String orderStatus;
    private String message;
}
