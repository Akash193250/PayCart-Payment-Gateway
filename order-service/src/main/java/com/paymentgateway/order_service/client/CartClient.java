package com.paymentgateway.order_service.client;

import com.paymentgateway.order_service.dto.CartResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class CartClient {

    private final RestClient restClient;

    public CartClient(
            @Qualifier("loadBalancedRestClientBuilder") RestClient.Builder restClientBuilder) {

        this.restClient = restClientBuilder
                .baseUrl("http://CART-SERVICE")
                .build();
    }

    public CartResponse getCart(String authorizationHeader) {

        return restClient.get()
                .uri("/api/cart")
                .header("Authorization", authorizationHeader)
                .retrieve()
                .body(CartResponse.class);
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