package com.paymentgateway.payment_service.service;

import com.paymentgateway.payment_service.client.OrderClient;
import com.paymentgateway.payment_service.dao.PaymentDAO;
import com.paymentgateway.payment_service.dto.CreatePaymentRequest;
import com.paymentgateway.payment_service.dto.OrderResponse;
import com.paymentgateway.payment_service.dto.PaymentResponse;
import com.paymentgateway.payment_service.model.Payment;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentService {

        private final PaymentDAO paymentDAO;
        private final OrderClient orderClient;

        public PaymentService(
                        PaymentDAO paymentDAO,
                        OrderClient orderClient) {

                this.paymentDAO = paymentDAO;
                this.orderClient = orderClient;
        }

        public PaymentResponse createPayment(
                        CreatePaymentRequest request,
                        String authorizationHeader,
                        Authentication authentication)
                        throws Exception {

                String userEmail = authentication.getName();

                OrderResponse order = orderClient.getOrder(
                                request.getOrderId(),
                                authorizationHeader);

                if (order == null) {
                        throw new IllegalArgumentException("Order not found");
                }

                if (!userEmail.equals(order.getUserEmail())) {
                        throw new IllegalArgumentException(
                                        "You cannot pay for this order");
                }

                if (!"PENDING_PAYMENT".equals(order.getStatus())) {
                        throw new IllegalArgumentException(
                                        "Order is not eligible for payment");
                }

                BigDecimal amount = order.getTotalAmount();

                long amountInPaise = amount
                                .multiply(BigDecimal.valueOf(100))
                                .longValueExact();

                PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                                .setAmount(amountInPaise)
                                .setCurrency("inr")

                                .setAutomaticPaymentMethods(
                                                PaymentIntentCreateParams.AutomaticPaymentMethods
                                                                .builder()
                                                                .setEnabled(true)
                                                                .setAllowRedirects(
                                                                                PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER)
                                                                .build())

                                .putMetadata(
                                                "orderId",
                                                order.getOrderId().toString())

                                .putMetadata(
                                                "userEmail",
                                                userEmail)

                                .build();

                PaymentIntent paymentIntent = PaymentIntent.create(params);

                Payment payment = new Payment();
                payment.setOrderId(order.getOrderId());
                payment.setUserEmail(userEmail);
                payment.setAmount(amount);
                payment.setCurrency("INR");
                payment.setStripePaymentIntentId(paymentIntent.getId());
                payment.setStatus(paymentIntent.getStatus());

                Long paymentId = paymentDAO.createPayment(payment);

                return new PaymentResponse(
                                paymentId,
                                order.getOrderId(),
                                amount,
                                "INR",
                                paymentIntent.getStatus(),
                                paymentIntent.getClientSecret());
        }

}