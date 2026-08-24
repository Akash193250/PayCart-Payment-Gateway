package com.paymentgateway.cart_service.dto;

import com.paymentgateway.cart_service.model.CartItem;

import java.math.BigDecimal;
import java.util.List;

public class CartResponse {

    private Long cartId;
    private String userEmail;
    private List<CartItem> items;
    private BigDecimal totalAmount;

    public CartResponse() {
    }

    public CartResponse(
            Long cartId,
            String userEmail,
            List<CartItem> items,
            BigDecimal totalAmount) {

        this.cartId = cartId;
        this.userEmail = userEmail;
        this.items = items;
        this.totalAmount = totalAmount;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}