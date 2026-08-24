package com.paymentgateway.cart_service.dao;

import com.paymentgateway.cart_service.model.Cart;
import com.paymentgateway.cart_service.model.CartItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class CartDAO {

        private final JdbcTemplate jdbcTemplate;

        public CartDAO(JdbcTemplate jdbcTemplate) {
                this.jdbcTemplate = jdbcTemplate;
        }

        public Optional<Cart> findCartByUserEmail(String userEmail) {

                String sql = """
                                SELECT id, user_email, created_at, updated_at
                                FROM carts
                                WHERE user_email = ?
                                """;

                List<Cart> carts = jdbcTemplate.query(
                                sql,
                                (rs, rowNum) -> {
                                        Cart cart = new Cart();
                                        cart.setId(rs.getLong("id"));
                                        cart.setUserEmail(rs.getString("user_email"));
                                        cart.setCreatedAt(rs.getTimestamp("created_at"));
                                        cart.setUpdatedAt(rs.getTimestamp("updated_at"));
                                        return cart;
                                },
                                userEmail);

                return carts.stream().findFirst();
        }

        public Cart createCart(String userEmail) {

                String sql = """
                                INSERT INTO carts (user_email)
                                VALUES (?)
                                """;

                KeyHolder keyHolder = new GeneratedKeyHolder();

                jdbcTemplate.update(connection -> {

                        PreparedStatement statement = connection.prepareStatement(
                                        sql,
                                        Statement.RETURN_GENERATED_KEYS);

                        statement.setString(1, userEmail);
                        return statement;

                }, keyHolder);

                Number generatedId = keyHolder.getKey();

                if (generatedId == null) {
                        throw new IllegalStateException("Failed to create cart");
                }

                Cart cart = new Cart();
                cart.setId(generatedId.longValue());
                cart.setUserEmail(userEmail);

                return cart;
        }

        public Cart findOrCreateCart(String userEmail) {

                return findCartByUserEmail(userEmail)
                                .orElseGet(() -> createCart(userEmail));
        }

        public Optional<CartItem> findItemByCartAndProduct(
                        Long cartId,
                        Long productId) {

                String sql = """
                                SELECT id, cart_id, product_id, product_name,
                                       unit_price, quantity, created_at
                                FROM cart_items
                                WHERE cart_id = ? AND product_id = ?
                                """;

                List<CartItem> items = jdbcTemplate.query(
                                sql,
                                (rs, rowNum) -> mapCartItem(rs),
                                cartId,
                                productId);

                return items.stream().findFirst();
        }

        public int addItem(CartItem item) {

                String sql = """
                                INSERT INTO cart_items
                                (cart_id, product_id, product_name, unit_price, quantity)
                                VALUES (?, ?, ?, ?, ?)
                                """;

                return jdbcTemplate.update(
                                sql,
                                item.getCartId(),
                                item.getProductId(),
                                item.getProductName(),
                                item.getUnitPrice(),
                                item.getQuantity());
        }

        public int increaseQuantity(
                        Long itemId,
                        Integer quantityToAdd) {

                String sql = """
                                UPDATE cart_items
                                SET quantity = quantity + ?
                                WHERE id = ?
                                """;

                return jdbcTemplate.update(
                                sql,
                                quantityToAdd,
                                itemId);
        }

        public int updateQuantity(
                        Long itemId,
                        Long cartId,
                        Integer quantity) {

                String sql = """
                                UPDATE cart_items
                                SET quantity = ?
                                WHERE id = ? AND cart_id = ?
                                """;

                return jdbcTemplate.update(
                                sql,
                                quantity,
                                itemId,
                                cartId);
        }

        public List<CartItem> findItemsByCartId(Long cartId) {

                String sql = """
                                SELECT id, cart_id, product_id, product_name,
                                       unit_price, quantity, created_at
                                FROM cart_items
                                WHERE cart_id = ?
                                ORDER BY id DESC
                                """;

                return jdbcTemplate.query(
                                sql,
                                (rs, rowNum) -> mapCartItem(rs),
                                cartId);
        }

        public int removeItem(
                        Long itemId,
                        Long cartId) {

                String sql = """
                                DELETE FROM cart_items
                                WHERE id = ? AND cart_id = ?
                                """;

                return jdbcTemplate.update(
                                sql,
                                itemId,
                                cartId);
        }

        public int clearCart(Long cartId) {

                String sql = """
                                DELETE FROM cart_items
                                WHERE cart_id = ?
                                """;

                return jdbcTemplate.update(sql, cartId);
        }

        private CartItem mapCartItem(java.sql.ResultSet rs)
                        throws java.sql.SQLException {

                CartItem item = new CartItem();

                item.setId(rs.getLong("id"));
                item.setCartId(rs.getLong("cart_id"));
                item.setProductId(rs.getLong("product_id"));
                item.setProductName(rs.getString("product_name"));
                item.setUnitPrice(rs.getBigDecimal("unit_price"));
                item.setQuantity(rs.getInt("quantity"));
                item.setCreatedAt(rs.getTimestamp("created_at"));

                return item;
        }
}