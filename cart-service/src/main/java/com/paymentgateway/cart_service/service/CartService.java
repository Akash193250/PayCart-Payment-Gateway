package com.paymentgateway.cart_service.service;

import com.paymentgateway.cart_service.client.ProductClient;
import com.paymentgateway.cart_service.dao.CartDAO;
import com.paymentgateway.cart_service.dto.AddToCartRequest;
import com.paymentgateway.cart_service.dto.CartResponse;
import com.paymentgateway.cart_service.dto.ProductResponse;
import com.paymentgateway.cart_service.dto.UpdateQuantityRequest;
import com.paymentgateway.cart_service.model.Cart;
import com.paymentgateway.cart_service.model.CartItem;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService {

        private final CartDAO cartDAO;
        private final ProductClient productClient;

        public CartService(
                        CartDAO cartDAO,
                        ProductClient productClient) {

                this.cartDAO = cartDAO;
                this.productClient = productClient;
        }

        public String addToCart(
                        AddToCartRequest request,
                        String authorizationHeader,
                        Authentication authentication) {

                String userEmail = authentication.getName();

                ProductResponse product = productClient.getProductById(
                                request.getProductId(),
                                authorizationHeader);

                if (product == null) {
                        throw new IllegalArgumentException(
                                        "Product not found");
                }

                if (request.getQuantity() > product.getStock()) {
                        throw new IllegalArgumentException(
                                        "Requested quantity exceeds available stock");
                }

                Cart cart = cartDAO.findOrCreateCart(userEmail);

                var existingItem = cartDAO.findItemByCartAndProduct(
                                cart.getId(),
                                product.getId());

                if (existingItem.isPresent()) {

                        int newQuantity = existingItem.get().getQuantity()
                                        + request.getQuantity();

                        if (newQuantity > product.getStock()) {
                                throw new IllegalArgumentException(
                                                "Total cart quantity exceeds available stock");
                        }

                        cartDAO.increaseQuantity(
                                        existingItem.get().getId(),
                                        request.getQuantity());

                } else {

                        CartItem item = new CartItem();

                        item.setCartId(cart.getId());
                        item.setProductId(product.getId());
                        item.setProductName(product.getName());
                        item.setUnitPrice(product.getPrice());
                        item.setQuantity(request.getQuantity());

                        cartDAO.addItem(item);
                }

                return "Product added to cart successfully";
        }

        public CartResponse getCart(
                        Authentication authentication) {

                String userEmail = authentication.getName();

                Cart cart = cartDAO.findOrCreateCart(userEmail);

                List<CartItem> items = cartDAO.findItemsByCartId(cart.getId());

                BigDecimal totalAmount = items.stream()
                                .map(item -> item.getUnitPrice().multiply(
                                                BigDecimal.valueOf(
                                                                item.getQuantity())))
                                .reduce(
                                                BigDecimal.ZERO,
                                                BigDecimal::add);

                return new CartResponse(
                                cart.getId(),
                                userEmail,
                                items,
                                totalAmount);
        }

        public String updateQuantity(
                        Long itemId,
                        UpdateQuantityRequest request,
                        Authentication authentication) {

                String userEmail = authentication.getName();

                Cart cart = cartDAO.findOrCreateCart(userEmail);

                int rowsAffected = cartDAO.updateQuantity(
                                itemId,
                                cart.getId(),
                                request.getQuantity());

                if (rowsAffected != 1) {
                        throw new IllegalArgumentException(
                                        "Cart item not found");
                }

                return "Cart quantity updated successfully";
        }

        public String removeItem(
                        Long itemId,
                        Authentication authentication) {

                String userEmail = authentication.getName();

                Cart cart = cartDAO.findOrCreateCart(userEmail);

                int rowsAffected = cartDAO.removeItem(itemId, cart.getId());

                if (rowsAffected != 1) {
                        throw new IllegalArgumentException(
                                        "Cart item not found");
                }

                return "Cart item removed successfully";
        }

        public String clearCart(
                        Authentication authentication) {

                String userEmail = authentication.getName();

                Cart cart = cartDAO.findOrCreateCart(userEmail);

                cartDAO.clearCart(cart.getId());

                return "Cart cleared successfully";
        }

        public void clearCartByEmail(String userEmail) {

                Cart cart = cartDAO.findCartByUserEmail(userEmail)
                                .orElse(null);

                if (cart != null) {
                        cartDAO.clearCart(cart.getId());
                }
        }
}