package com.paymentgateway.payment_service.dao;

import com.paymentgateway.payment_service.model.Payment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class PaymentDAO {

        private final JdbcTemplate jdbcTemplate;

        public PaymentDAO(JdbcTemplate jdbcTemplate) {
                this.jdbcTemplate = jdbcTemplate;
        }

        public Long createPayment(Payment payment) {

                String sql = """
                                INSERT INTO payments
                                (order_id, user_email, amount, currency,
                                 stripe_payment_intent_id, status)
                                VALUES (?, ?, ?, ?, ?, ?)
                                """;

                KeyHolder keyHolder = new GeneratedKeyHolder();

                jdbcTemplate.update(connection -> {

                        PreparedStatement statement = connection.prepareStatement(
                                        sql,
                                        Statement.RETURN_GENERATED_KEYS);

                        statement.setLong(1, payment.getOrderId());
                        statement.setString(2, payment.getUserEmail());
                        statement.setBigDecimal(3, payment.getAmount());
                        statement.setString(4, payment.getCurrency());
                        statement.setString(5, payment.getStripePaymentIntentId());
                        statement.setString(6, payment.getStatus());

                        return statement;

                }, keyHolder);

                Number key = keyHolder.getKey();

                if (key == null) {
                        throw new IllegalStateException(
                                        "Failed to create payment record");
                }

                return key.longValue();
        }

        public Payment findById(Long paymentId) {

                String sql = """
                                SELECT id, order_id, user_email, amount,
                                       currency, stripe_payment_intent_id,
                                       status, created_at, updated_at
                                FROM payments
                                WHERE id = ?
                                """;

                return jdbcTemplate.queryForObject(
                                sql,
                                (rs, rowNum) -> {

                                        Payment payment = new Payment();

                                        payment.setId(rs.getLong("id"));
                                        payment.setOrderId(rs.getLong("order_id"));
                                        payment.setUserEmail(rs.getString("user_email"));
                                        payment.setAmount(rs.getBigDecimal("amount"));
                                        payment.setCurrency(rs.getString("currency"));
                                        payment.setStripePaymentIntentId(
                                                        rs.getString("stripe_payment_intent_id"));
                                        payment.setStatus(rs.getString("status"));
                                        payment.setCreatedAt(rs.getTimestamp("created_at"));
                                        payment.setUpdatedAt(rs.getTimestamp("updated_at"));

                                        return payment;
                                },
                                paymentId);
        }

        public int updateStatus(
                        String stripePaymentIntentId,
                        String status) {

                String sql = """
                                UPDATE payments
                                SET status = ?
                                WHERE stripe_payment_intent_id = ?
                                """;

                return jdbcTemplate.update(
                                sql,
                                status,
                                stripePaymentIntentId);
        }
}