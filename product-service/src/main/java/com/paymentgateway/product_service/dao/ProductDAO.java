package com.paymentgateway.product_service.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.paymentgateway.product_service.exception.ProductNotFoundException;
import com.paymentgateway.product_service.model.Product;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

@Repository
public class ProductDAO {

    private final JdbcTemplate jdbcTemplate;

    public ProductDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(Product product) {

        String sql = """
                INSERT INTO products
                (name, description, price, stock, category, image_url)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        return jdbcTemplate.update(
                sql,
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory(),
                product.getImageUrl());
    }

    public List<Product> findAll() {

        String sql = """
                SELECT *
                FROM products
                ORDER BY id DESC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {

            Product product = new Product();

            product.setId(rs.getLong("id"));
            product.setName(rs.getString("name"));
            product.setDescription(rs.getString("description"));
            product.setPrice(rs.getBigDecimal("price"));
            product.setStock(rs.getInt("stock"));
            product.setCategory(rs.getString("category"));
            product.setImageUrl(rs.getString("image_url"));
            product.setCreatedAt(rs.getTimestamp("created_at"));

            return product;
        });
    }

    public Product findById(Long id) {

        String sql = """
                SELECT *
                FROM products
                WHERE id = ?
                """;

        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    (rs, rowNum) -> {

                        Product product = new Product();

                        product.setId(rs.getLong("id"));
                        product.setName(rs.getString("name"));
                        product.setDescription(rs.getString("description"));
                        product.setPrice(rs.getBigDecimal("price"));
                        product.setStock(rs.getInt("stock"));
                        product.setCategory(rs.getString("category"));
                        product.setImageUrl(rs.getString("image_url"));
                        product.setCreatedAt(rs.getTimestamp("created_at"));

                        return product;
                    },
                    id);

        } catch (EmptyResultDataAccessException exception) {
            throw new ProductNotFoundException(id);
        }
    }

    public int delete(Long id) {

        String sql = """
                DELETE FROM products
                WHERE id = ?
                """;

        return jdbcTemplate.update(sql, id);
    }

    public int update(Long id, Product product) {

        String sql = """
                UPDATE products
                SET name = ?,
                    description = ?,
                    price = ?,
                    stock = ?,
                    category = ?,
                    image_url = ?
                WHERE id = ?
                """;

        return jdbcTemplate.update(
                sql,
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory(),
                product.getImageUrl(),
                id);
    }
}