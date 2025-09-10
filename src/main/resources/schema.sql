CREATE DATABASE IF NOT EXISTS `food-delivery-db`;

USE `food-delivery-db`;

CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    items TEXT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    order_time TIMESTAMP NOT NULL,
    order_status VARCHAR(50) DEFAULT 'PLACED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_customer_name (customer_name),
    INDEX idx_order_status (order_status),
    INDEX idx_order_time (order_time)
);
