package com.paymentgateway.order_service.controller;

import com.paymentgateway.order_service.dto.OrderResponse;
import com.paymentgateway.order_service.model.Order;
import com.paymentgateway.order_service.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Hidden;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

        private final OrderService orderService;

        public OrderController(
                        OrderService orderService) {

                this.orderService = orderService;
        }

        @PostMapping
        public ResponseEntity<OrderResponse> createOrder(
                        @RequestHeader("Authorization") String authorizationHeader,
                        Authentication authentication) {

                return ResponseEntity.ok(
                                orderService.createOrder(
                                                authorizationHeader,
                                                authentication));
        }

        @GetMapping("/{orderId}")
        public ResponseEntity<OrderResponse> getOrder(
                        @PathVariable Long orderId,
                        Authentication authentication) {

                return ResponseEntity.ok(
                                orderService.getOrder(
                                                orderId,
                                                authentication));
        }

        @GetMapping
        public ResponseEntity<List<Order>> getMyOrders(
                        Authentication authentication) {

                return ResponseEntity.ok(
                                orderService.getMyOrders(
                                                authentication));
        }

        @Hidden
        @PutMapping("/{orderId}/status")
        public ResponseEntity<String> updateOrderStatus(
                        @PathVariable Long orderId,
                        @RequestParam String status) {

                orderService.updateOrderStatus(orderId, status);

                return ResponseEntity.ok(
                                "Order status updated successfully");
        }
}