package com.paymentgateway.payment_service.dto;

public class NotificationRequest {

    private String userEmail;
    private Long orderId;
    private String type;
    private String subject;
    private String message;

    public NotificationRequest() {
    }

    public NotificationRequest(
            String userEmail,
            Long orderId,
            String type,
            String subject,
            String message) {

        this.userEmail = userEmail;
        this.orderId = orderId;
        this.type = type;
        this.subject = subject;
        this.message = message;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}