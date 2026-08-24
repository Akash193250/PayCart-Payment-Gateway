package com.paymentgateway.order_service.dao;

import com.paymentgateway.order_service.model.Order;
import com.paymentgateway.order_service.model.OrderItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class OrderDAO {

    private final JdbcTemplate jdbcTemplate;

    public OrderDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long createOrder(Order order) {

        String sql = """
                INSERT INTO orders
                (user_email, total_amount, status)
                VALUES (?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {

            PreparedStatement statement = connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, order.getUserEmail());
            statement.setBigDecimal(2, order.getTotalAmount());
            statement.setString(3, order.getStatus());

            return statement;

        }, keyHolder);

        Number key = keyHolder.getKey();

        if (key == null) {
            throw new IllegalStateException("Failed to create order");
        }

        return key.longValue();
    }

    public int addOrderItem(OrderItem item) {

        String sql = """
                INSERT INTO order_items
                (order_id, product_id, product_name,
                 unit_price, quantity, subtotal)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        return jdbcTemplate.update(
                sql,
                item.getOrderId(),
                item.getProductId(),
                item.getProductName(),
                item.getUnitPrice(),
                item.getQuantity(),
                item.getSubtotal());
    }

    public Order findOrderById(Long orderId) {

        String sql = """
                SELECT id, user_email, total_amount,
                       status, created_at
                FROM orders
                WHERE id = ?
                """;

        return jdbcTemplate.queryForObject(
                sql,
                (rs, rowNum) -> {

                    Order order = new Order();

                    order.setId(rs.getLong("id"));
                    order.setUserEmail(rs.getString("user_email"));
                    order.setTotalAmount(rs.getBigDecimal("total_amount"));
                    order.setStatus(rs.getString("status"));
                    order.setCreatedAt(rs.getTimestamp("created_at"));

                    return order;
                },
                orderId);
    }

    public List<OrderItem> findItemsByOrderId(Long orderId) {

        String sql = """
                SELECT id, order_id, product_id, product_name,
                       unit_price, quantity, subtotal
                FROM order_items
                WHERE order_id = ?
                """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> {

                    OrderItem item = new OrderItem();

                    item.setId(rs.getLong("id"));
                    item.setOrderId(rs.getLong("order_id"));
                    item.setProductId(rs.getLong("product_id"));
                    item.setProductName(rs.getString("product_name"));
                    item.setUnitPrice(rs.getBigDecimal("unit_price"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setSubtotal(rs.getBigDecimal("subtotal"));

                    return item;
                },
                orderId);
    }

    public List<Order> findOrdersByUserEmail(String userEmail) {

        String sql = """
                SELECT id, user_email, total_amount,
                       status, created_at
                FROM orders
                WHERE user_email = ?
                ORDER BY created_at DESC
                """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> {

                    Order order = new Order();

                    order.setId(rs.getLong("id"));
                    order.setUserEmail(rs.getString("user_email"));
                    order.setTotalAmount(rs.getBigDecimal("total_amount"));
                    order.setStatus(rs.getString("status"));
                    order.setCreatedAt(rs.getTimestamp("created_at"));

                    return order;
                },
                userEmail);
    }

    public int updateOrderStatus(
            Long orderId,
            String status) {

        String sql = """
                UPDATE orders
                SET status = ?
                WHERE id = ?
                """;

        return jdbcTemplate.update(
                sql,
                status,
                orderId);
    }
}