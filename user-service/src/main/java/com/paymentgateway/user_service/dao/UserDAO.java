package com.paymentgateway.user_service.dao;

import com.paymentgateway.user_service.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO {

    private final JdbcTemplate jdbcTemplate;

    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(User user) {

        String sql = """
                INSERT INTO users
                (full_name, email, password, role)
                VALUES (?, ?, ?, ?)
                """;

        return jdbcTemplate.update(
                sql,
                user.getFullName(),
                user.getEmail(),
                user.getPassword(),
                user.getRole());
    }

    public boolean existsByEmail(String email) {

        String sql = """
                SELECT COUNT(*)
                FROM users
                WHERE email = ?
                """;

        Integer count = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                email);

        return count != null && count > 0;
    }

    public User findByEmail(String email) {

        String sql = """
                SELECT id, full_name, email, password, role
                FROM users
                WHERE email = ?
                """;

        return jdbcTemplate.queryForObject(
                sql,
                (resultSet, rowNumber) -> {

                    User user = new User();

                    user.setId(resultSet.getLong("id"));
                    user.setFullName(resultSet.getString("full_name"));
                    user.setEmail(resultSet.getString("email"));
                    user.setPassword(resultSet.getString("password"));
                    user.setRole(resultSet.getString("role"));

                    return user;
                },
                email);
    }
}