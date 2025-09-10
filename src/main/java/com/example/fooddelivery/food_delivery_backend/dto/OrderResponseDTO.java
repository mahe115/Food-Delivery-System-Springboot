package com.example.fooddelivery.food_delivery_backend.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderResponseDTO {
    private Long id;
    private String customerName;
    private String items;
    private BigDecimal totalAmount;
    private LocalDateTime orderTime;
    private String orderStatus;
    private LocalDateTime createdAt;
}
