package com.paymentgateway.cart_service.controller;

import com.paymentgateway.cart_service.dto.AddToCartRequest;
import com.paymentgateway.cart_service.dto.CartResponse;
import com.paymentgateway.cart_service.dto.UpdateQuantityRequest;
import com.paymentgateway.cart_service.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Hidden;

@RestController
@RequestMapping("/api/cart")
public class CartController {

        private final CartService cartService;

        public CartController(CartService cartService) {
                this.cartService = cartService;
        }

        // Add product to cart
        @PostMapping("/add")
        public ResponseEntity<String> addToCart(
                        @Valid @RequestBody AddToCartRequest request,
                        @RequestHeader("Authorization") String authorizationHeader,
                        Authentication authentication) {

                return ResponseEntity.ok(
                                cartService.addToCart(
                                                request,
                                                authorizationHeader,
                                                authentication));
        }

        // View logged-in user's cart
        @GetMapping
        public ResponseEntity<CartResponse> getCart(
                        Authentication authentication) {

                return ResponseEntity.ok(
                                cartService.getCart(authentication));
        }

        // Update cart item quantity
        @PutMapping("/update/{itemId}")
        public ResponseEntity<String> updateQuantity(
                        @PathVariable Long itemId,
                        @Valid @RequestBody UpdateQuantityRequest request,
                        Authentication authentication) {

                return ResponseEntity.ok(
                                cartService.updateQuantity(
                                                itemId,
                                                request,
                                                authentication));
        }

        // Remove one cart item
        @Hidden
        @DeleteMapping("/remove/{itemId}")
        public ResponseEntity<String> removeItem(
                        @PathVariable Long itemId,
                        Authentication authentication) {

                return ResponseEntity.ok(
                                cartService.removeItem(
                                                itemId,
                                                authentication));
        }

        // Clear logged-in user's cart
        @DeleteMapping("/clear")
        public ResponseEntity<String> clearCart(
                        Authentication authentication) {

                return ResponseEntity.ok(
                                cartService.clearCart(authentication));
        }

        // Internal endpoint used after successful payment
        @DeleteMapping("/internal/clear")
        public ResponseEntity<String> clearCartByEmail(
                        @RequestParam String userEmail) {

                cartService.clearCartByEmail(userEmail);

                return ResponseEntity.ok(
                                "Cart cleared successfully");
        }
}