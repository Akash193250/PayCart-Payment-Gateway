package com.paymentgateway.order_service.service;

import com.paymentgateway.order_service.client.CartClient;
import com.paymentgateway.order_service.dao.OrderDAO;
import com.paymentgateway.order_service.dto.CartItemResponse;
import com.paymentgateway.order_service.dto.CartResponse;
import com.paymentgateway.order_service.dto.OrderResponse;
import com.paymentgateway.order_service.model.Order;
import com.paymentgateway.order_service.model.OrderItem;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

        private final OrderDAO orderDAO;
        private final CartClient cartClient;

        public OrderService(
                        OrderDAO orderDAO,
                        CartClient cartClient) {

                this.orderDAO = orderDAO;
                this.cartClient = cartClient;
        }

        @Transactional
        public OrderResponse createOrder(
                        String authorizationHeader,
                        Authentication authentication) {

                String userEmail = authentication.getName();

                CartResponse cart = cartClient.getCart(authorizationHeader);

                if (cart == null ||
                                cart.getItems() == null ||
                                cart.getItems().isEmpty()) {

                        throw new IllegalArgumentException(
                                        "Cannot create order because cart is empty");
                }

                Order order = new Order();

                order.setUserEmail(userEmail);
                order.setTotalAmount(cart.getTotalAmount());
                order.setStatus("PENDING_PAYMENT");

                Long orderId = orderDAO.createOrder(order);

                for (CartItemResponse cartItem : cart.getItems()) {

                        OrderItem orderItem = new OrderItem();

                        orderItem.setOrderId(orderId);
                        orderItem.setProductId(
                                        cartItem.getProductId());

                        orderItem.setProductName(
                                        cartItem.getProductName());

                        orderItem.setUnitPrice(
                                        cartItem.getUnitPrice());

                        orderItem.setQuantity(
                                        cartItem.getQuantity());

                        BigDecimal subtotal = cartItem.getUnitPrice()
                                        .multiply(
                                                        BigDecimal.valueOf(
                                                                        cartItem.getQuantity()));

                        orderItem.setSubtotal(subtotal);

                        orderDAO.addOrderItem(orderItem);
                }

                Order savedOrder = orderDAO.findOrderById(orderId);

                List<OrderItem> items = orderDAO.findItemsByOrderId(orderId);

                return new OrderResponse(
                                savedOrder.getId(),
                                savedOrder.getUserEmail(),
                                savedOrder.getTotalAmount(),
                                savedOrder.getStatus(),
                                savedOrder.getCreatedAt(),
                                items);
        }

        public OrderResponse getOrder(
                        Long orderId,
                        Authentication authentication) {

                Order order = orderDAO.findOrderById(orderId);

                if (!order.getUserEmail()
                                .equals(authentication.getName())) {

                        throw new IllegalArgumentException(
                                        "You cannot access this order");
                }

                List<OrderItem> items = orderDAO.findItemsByOrderId(orderId);

                return new OrderResponse(
                                order.getId(),
                                order.getUserEmail(),
                                order.getTotalAmount(),
                                order.getStatus(),
                                order.getCreatedAt(),
                                items);
        }

        public List<Order> getMyOrders(
                        Authentication authentication) {

                return orderDAO.findOrdersByUserEmail(
                                authentication.getName());
        }

        public void updateOrderStatus(
                        Long orderId,
                        String status) {

                int rowsAffected = orderDAO.updateOrderStatus(
                                orderId,
                                status);

                if (rowsAffected != 1) {
                        throw new IllegalArgumentException(
                                        "Order not found");
                }
        }
}