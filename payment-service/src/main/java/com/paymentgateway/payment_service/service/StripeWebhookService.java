package com.paymentgateway.payment_service.service;

import com.paymentgateway.payment_service.dao.PaymentDAO;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.paymentgateway.payment_service.client.CartClient;
import com.paymentgateway.payment_service.client.OrderClient;

import com.paymentgateway.payment_service.client.NotificationClient;
import com.paymentgateway.payment_service.dto.NotificationRequest;

@Service
public class StripeWebhookService {

        private final PaymentDAO paymentDAO;
        private final OrderClient orderClient;
        private final CartClient cartClient;
        private final NotificationClient notificationClient;

        @Value("${stripe.webhook.secret}")

        private String webhookSecret;

        public StripeWebhookService(
                        PaymentDAO paymentDAO,
                        OrderClient orderClient,
                        CartClient cartClient,
                        NotificationClient notificationClient) {

                this.paymentDAO = paymentDAO;
                this.orderClient = orderClient;
                this.cartClient = cartClient;
                this.notificationClient = notificationClient;
        }

        public void handleWebhook(
                        String payload,
                        String signatureHeader) {

                try {

                        Event event = Webhook.constructEvent(
                                        payload,
                                        signatureHeader,
                                        webhookSecret);

                        switch (event.getType()) {

                                case "payment_intent.succeeded" -> {

                                        var deserializer = event.getDataObjectDeserializer();
                                        var stripeObject = deserializer.getObject();

                                        if (stripeObject.isEmpty()) {
                                                throw new IllegalStateException(
                                                                "Unable to deserialize PaymentIntent. " +
                                                                                "Event API version: "
                                                                                + event.getApiVersion());
                                        }

                                        PaymentIntent paymentIntent = (PaymentIntent) stripeObject.get();

                                        System.out.println(
                                                        "PAYMENT SUCCESS: " + paymentIntent.getId());

                                        // Update payment status
                                        paymentDAO.updateStatus(
                                                        paymentIntent.getId(),
                                                        "SUCCEEDED");

                                        // Get orderId from Stripe metadata
                                        String orderIdValue = paymentIntent.getMetadata().get("orderId");

                                        System.out.println(
                                                        "WEBHOOK ORDER ID: " + orderIdValue);

                                        if (orderIdValue != null) {

                                                Long orderId = Long.valueOf(orderIdValue);

                                                orderClient.updateOrderStatus(
                                                                orderId,
                                                                "PAID");

                                                System.out.println(
                                                                "ORDER UPDATED TO PAID");
                                        }

                                        // Get email from Stripe metadata
                                        String userEmail = paymentIntent.getMetadata().get("userEmail");

                                        System.out.println("WEBHOOK USER EMAIL: " + userEmail);

                                        if (userEmail != null && !userEmail.isBlank()) {

                                                try {

                                                        System.out.println(
                                                                        "CALLING CART CLEAR FOR: " + userEmail);

                                                        cartClient.clearCartByEmail(userEmail);

                                                        System.out.println(
                                                                        "CART CLEAR SUCCESSFUL");

                                                } catch (Exception e) {

                                                        System.out.println(
                                                                        "CART CLEAR FAILED: " + e.getMessage());

                                                        e.printStackTrace();
                                                }
                                        }

                                        // Send payment success notification
                                        if (userEmail != null
                                                        && !userEmail.isBlank()
                                                        && orderIdValue != null
                                                        && !orderIdValue.isBlank()) {

                                                try {

                                                        Long orderId = Long.valueOf(orderIdValue);

                                                        NotificationRequest notificationRequest = new NotificationRequest(
                                                                        userEmail,
                                                                        orderId,
                                                                        "PAYMENT_SUCCESS",
                                                                        "Payment Successful - Order #" + orderId,
                                                                        "Your payment for Order #" +
                                                                                        orderId +
                                                                                        " was successful. " +
                                                                                        "Your order has been confirmed.");

                                                        System.out.println(
                                                                        "CALLING NOTIFICATION SERVICE FOR ORDER: "
                                                                                        + orderId);

                                                        notificationClient.sendNotification(
                                                                        notificationRequest);

                                                        System.out.println(
                                                                        "NOTIFICATION SENT FOR ORDER: "
                                                                                        + orderId);

                                                } catch (Exception e) {

                                                        System.out.println(
                                                                        "NOTIFICATION FAILED: "
                                                                                        + e.getMessage());

                                                        e.printStackTrace();
                                                }
                                        }
                                }

                                case "payment_intent.payment_failed" -> {

                                        var deserializer = event.getDataObjectDeserializer();

                                        var stripeObject = deserializer.getObject();

                                        if (stripeObject.isEmpty()) {

                                                throw new IllegalStateException(
                                                                "Unable to deserialize PaymentIntent. " +
                                                                                "Event API version: "
                                                                                + event.getApiVersion());
                                        }

                                        PaymentIntent paymentIntent = (PaymentIntent) stripeObject.get();

                                        paymentDAO.updateStatus(
                                                        paymentIntent.getId(),
                                                        "FAILED");

                                        System.out.println(
                                                        "PAYMENT FAILED: " +
                                                                        paymentIntent.getId());
                                }

                                default -> {
                                        // Ignore events we don't currently use.
                                }
                        }

                } catch (Exception exception) {

                        throw new IllegalArgumentException(
                                        "Invalid Stripe webhook",
                                        exception);
                }
        }
}