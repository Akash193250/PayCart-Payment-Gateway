package com.paymentgateway.payment_service.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class CartClient {

        private final RestClient restClient;

        public CartClient(
                        @Qualifier("loadBalancedRestClientBuilder") RestClient.Builder builder) {

                this.restClient = builder
                                .baseUrl("http://CART-SERVICE")
                                .build();
        }

        public void clearCartByEmail(String userEmail) {

                System.out.println(
                                "CART CLIENT CALLED FOR: " + userEmail);

                restClient.delete()
                                .uri(uriBuilder -> uriBuilder
                                                .path("/api/cart/internal/clear")
                                                .queryParam("userEmail", userEmail)
                                                .build())
                                .retrieve()
                                .toBodilessEntity();

                System.out.println(
                                "CART SERVICE DELETE COMPLETED");
        }
}