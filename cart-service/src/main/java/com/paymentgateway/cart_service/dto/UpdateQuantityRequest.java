package com.paymentgateway.cart_service.dto;

import jakarta.validation.constraints.Min;

public class UpdateQuantityRequest {

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    public UpdateQuantityRequest() {
    }

    public UpdateQuantityRequest(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}