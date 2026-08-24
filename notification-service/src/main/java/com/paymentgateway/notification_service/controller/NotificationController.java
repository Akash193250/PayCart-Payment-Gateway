package com.paymentgateway.notification_service.controller;

import com.paymentgateway.notification_service.dto.NotificationRequest;
import com.paymentgateway.notification_service.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService) {

        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<String> createNotification(
            @Valid @RequestBody NotificationRequest request) {

        return ResponseEntity.ok(
                notificationService.createNotification(request));
    }
}