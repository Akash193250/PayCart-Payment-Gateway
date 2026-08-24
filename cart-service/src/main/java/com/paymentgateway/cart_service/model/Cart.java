package com.paymentgateway.cart_service.model;

import java.sql.Timestamp;

public class Cart {

    private Long id;
    private String userEmail;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Cart() {
    }

    public Cart(Long id,
            String userEmail,
            Timestamp createdAt,
            Timestamp updatedAt) {
        this.id = id;
        this.userEmail = userEmail;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}