package com.paymentgateway.order_service.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Order {

    private Long id;
    private String userEmail;
    private BigDecimal totalAmount;
    private String status;
    private Timestamp createdAt;

    public Order() {
    }

    public Order(
            Long id,
            String userEmail,
            BigDecimal totalAmount,
            String status,
            Timestamp createdAt) {

        this.id = id;
        this.userEmail = userEmail;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}