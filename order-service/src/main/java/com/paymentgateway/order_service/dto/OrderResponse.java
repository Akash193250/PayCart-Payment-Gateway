package com.paymentgateway.order_service.dto;

import com.paymentgateway.order_service.model.OrderItem;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class OrderResponse {

    private Long orderId;
    private String userEmail;
    private BigDecimal totalAmount;
    private String status;
    private Timestamp createdAt;
    private List<OrderItem> items;

    public OrderResponse() {
    }

    public OrderResponse(
            Long orderId,
            String userEmail,
            BigDecimal totalAmount,
            String status,
            Timestamp createdAt,
            List<OrderItem> items) {

        this.orderId = orderId;
        this.userEmail = userEmail;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
        this.items = items;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public List<OrderItem> getItems() {
        return items;
    }
}