package com.paymentgateway.payment_service.config;

import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {

    public StripeConfig(
            @Value("${stripe.secret.key}") String stripeSecretKey) {

        Stripe.apiKey = stripeSecretKey;

        System.out.println("STRIPE JAVA API VERSION: " + Stripe.API_VERSION);
    }

}