package com.paymentgateway.notification_service.dao;

import com.paymentgateway.notification_service.model.Notification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class NotificationDAO {

    private final JdbcTemplate jdbcTemplate;

    public NotificationDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(Notification notification) {

        String sql = """
                INSERT INTO notifications
                (user_email, order_id, type, subject, message, status)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        return jdbcTemplate.update(
                sql,
                notification.getUserEmail(),
                notification.getOrderId(),
                notification.getType(),
                notification.getSubject(),
                notification.getMessage(),
                notification.getStatus());
    }

    public int updateStatus(
            Long orderId,
            String type,
            String status) {

        String sql = """
                UPDATE notifications
                SET status = ?
                WHERE order_id = ?
                  AND type = ?
                """;

        return jdbcTemplate.update(
                sql,
                status,
                orderId,
                type);
    }
}