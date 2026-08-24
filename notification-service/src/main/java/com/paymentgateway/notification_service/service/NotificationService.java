package com.paymentgateway.notification_service.service;

import com.paymentgateway.notification_service.dao.NotificationDAO;
import com.paymentgateway.notification_service.dto.NotificationRequest;
import com.paymentgateway.notification_service.model.Notification;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final NotificationDAO notificationDAO;
    private final EmailService emailService;

    public NotificationService(
            NotificationDAO notificationDAO,
            EmailService emailService) {

        this.notificationDAO = notificationDAO;
        this.emailService = emailService;
    }

    public String createNotification(
            NotificationRequest request) {

        Notification notification = new Notification();

        notification.setUserEmail(request.getUserEmail());
        notification.setOrderId(request.getOrderId());
        notification.setType(request.getType());
        notification.setSubject(request.getSubject());
        notification.setMessage(request.getMessage());
        notification.setStatus("PENDING");

        int rowsAffected = notificationDAO.save(notification);

        if (rowsAffected != 1) {
            throw new IllegalStateException(
                    "Failed to save notification");
        }

        emailService.sendEmail(
                request.getUserEmail(),
                request.getSubject(),
                request.getMessage());

        notificationDAO.updateStatus(
                request.getOrderId(),
                request.getType(),
                "SENT");

        return "Notification sent successfully";
    }
}