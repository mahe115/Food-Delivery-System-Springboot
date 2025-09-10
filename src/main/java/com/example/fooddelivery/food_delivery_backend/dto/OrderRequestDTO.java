package com.example.fooddelivery.food_delivery_backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderRequestDTO {
    @NotBlank(message = "Customer name is required")
    @Size(max = 100, message = "Customer name must not exceed 100 characters")
    private String customerName;

    @NotBlank(message = "Items are required")
    private String items;

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.01", message = "Total amount must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Invalid total amount format")
    private BigDecimal totalAmount;

    @NotNull(message = "Order time is required")
    private LocalDateTime orderTime;
}
