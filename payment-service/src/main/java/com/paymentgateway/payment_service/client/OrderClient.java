package com.paymentgateway.payment_service.client;

import com.paymentgateway.payment_service.dto.OrderResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class OrderClient {

    private final RestClient restClient;

    public OrderClient(
            @Qualifier("loadBalancedRestClientBuilder") RestClient.Builder builder) {

        this.restClient = builder
                .baseUrl("http://ORDER-SERVICE")
                .build();
    }

    public OrderResponse getOrder(
            Long orderId,
            String authorizationHeader) {

        return restClient.get()
                .uri("/api/orders/{orderId}", orderId)
                .header("Authorization", authorizationHeader)
                .retrieve()
                .body(OrderResponse.class);
    }

    public void updateOrderStatus(
            Long orderId,
            String status) {

        restClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/orders/{orderId}/status")
                        .queryParam("status", status)
                        .build(orderId))
                .retrieve()
                .toBodilessEntity();
    }
}