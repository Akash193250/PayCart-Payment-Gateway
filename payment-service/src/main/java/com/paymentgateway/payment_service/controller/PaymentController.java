package com.paymentgateway.payment_service.controller;

import com.paymentgateway.payment_service.dto.CreatePaymentRequest;
import com.paymentgateway.payment_service.dto.PaymentResponse;
import com.paymentgateway.payment_service.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(
            PaymentService paymentService) {

        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
            @Valid @RequestBody CreatePaymentRequest request,

            @RequestHeader("Authorization") String authorizationHeader,

            Authentication authentication)
            throws Exception {

        return ResponseEntity.ok(
                paymentService.createPayment(
                        request,
                        authorizationHeader,
                        authentication));
    }
}