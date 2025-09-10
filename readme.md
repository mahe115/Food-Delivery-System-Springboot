```markdown
# Food Delivery Backend Service

## Overview

This project is a scalable backend service for processing food delivery orders, built with Java, Spring Boot, MySQL, and an in-memory queue for asynchronous order processing. It implements RESTful APIs allowing clients to place orders, fetch orders with pagination, track order status, and manually update the status. It follows best practices in validation, exception handling, DTO pattern, and service layer abstraction.

---

## Tech Stack

- Java 17+
- Spring Boot 3.x
- Spring Data JPA
- MySQL 8.x
- In-memory queue for asynchronous order processing
- Maven for build and dependency management

---

## Features

- Place an order with fields: `customer_name`, `items`, `total_amount`, `order_time`
- Fetch all orders with pagination and sorting support
- Input validation and comprehensive exception handling
- Persistent storage using MySQL with optimized schema and indexing
- Asynchronous processing of orders with background status update to `PROCESSED`
- APIs to fetch individual order status and to manually update order status
- Usage of DTOs and service layer abstraction for clean architecture
- Configurable in-memory queue to simulate asynchronous processing (AWS SQS can be integrated)

---

## Setup Instructions

### Prerequisites

- Java 17 or higher installed
- Maven installed and configured
- MySQL server running locally or accessible remotely
- Git (optional) for cloning the repo

### Database Setup

1. Start your MySQL server.
2. Create the database and tables by running the provided SQL script:

```
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
```

3. Update your MySQL credentials in `src/main/resources/application.properties`:

```
spring.datasource.url=jdbc:mysql://localhost:3306/food-delivery-db
spring.datasource.username=your_mysql_user
spring.datasource.password=your_mysql_password
```

---

### Build and Run

1. Clone the repository or download the source code.

2. Open a terminal at the project root.

3. Build the project using Maven:

```
mvn clean install
```

4. Run the Spring Boot application:

```
mvn spring-boot:run
```

The backend server will start on port `8080` by default.

---

## API Usage

### 1. Place an Order

- **Endpoint**: `POST /api/orders`
- **Headers**: `Content-Type: application/json`
- **Request Body Example**:

```
{
    "customerName": "Jane Doe",
    "items": "2x Burger, 1x Fries",
    "totalAmount": 15.75,
    "orderTime": "2025-09-10T18:00:00"
}
```

- **Response**: Created order JSON with status `PLACED` and generated ID.

---

### 2. Fetch All Orders (Paginated)

- **Endpoint**: `GET /api/orders`
- **Query Parameters**:
  - `page` (default `0`) — page number
  - `size` (default `10`) — page size
  - `sortBy` (default `orderTime`) — sort field
  - `sortDirection` (default `desc`) — `asc` or `desc`
- **Example**:  
`GET /api/orders?page=0&size=5&sortBy=orderTime&sortDirection=desc`

- **Response**: A paged list of orders

---

### 3. Get Order by ID

- **Endpoint**: `GET /api/orders/{orderId}`
- **Example**: `GET /api/orders/1`
- **Response**: JSON of the order details

---

### 4. Get Order Status

- **Endpoint**: `GET /api/orders/{orderId}/status`
- **Example**: `GET /api/orders/1/status`
- **Response**: JSON with order ID, current status, and a message

---

### 5. Update Order Status

- **Endpoint**: `PUT /api/orders/{orderId}/status?status=STATUS`
- **Replace** `STATUS` with one of: `PLACED`, `PROCESSING`, `PROCESSED`, `DELIVERED`, `CANCELLED`
- **Example**: `PUT /api/orders/1/status?status=PROCESSED`
- **Response**: Confirmation JSON with updated status

---

## Asynchronous Order Processing

- Orders placed are asynchronously processed by an in-memory queue (configurable).
- After a configurable delay (`queue.processing.delay` in milliseconds, default 5000), the order status is automatically updated to `PROCESSED`.
- Check logs for processing events.

---

## Configuration

All key configurations are located in `src/main/resources/application.properties`. Some important settings:

```
server.port=8080
queue.in-memory.enabled=true
queue.processing.delay=5000
```

- To switch to AWS SQS, configure AWS credentials and update the `QueueService` class.

---

## Validation & Exception Handling

- The application validates input data using Jakarta Bean Validation with clear error messages.
- Validation errors return HTTP 400 Bad Request with detailed error explanation.
- If resources are not found (e.g., invalid order ID), HTTP 404 Not Found with message is returned.
- Unexpected errors return HTTP 500 Internal Server Error with generic message.

---

## Contact & Support

For questions or issues, please reach out to the author or create issues in the project repository.

---

**Thank you for using this Food Delivery Backend Service!**
```