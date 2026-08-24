package com.paymentgateway.payment_service.client;

import com.paymentgateway.payment_service.dto.NotificationRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class NotificationClient {

    private final RestClient restClient;

    public NotificationClient(
            @Qualifier("loadBalancedRestClientBuilder") RestClient.Builder builder) {

        this.restClient = builder
                .baseUrl("http://NOTIFICATION-SERVICE")
                .build();
    }

    public void sendNotification(NotificationRequest request) {

        restClient.post()
                .uri("/api/notifications")
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }
}