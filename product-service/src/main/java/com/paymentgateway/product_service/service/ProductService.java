package com.paymentgateway.product_service.service;

import com.paymentgateway.product_service.dao.ProductDAO;
import com.paymentgateway.product_service.dto.ProductRequest;
import com.paymentgateway.product_service.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductDAO productDAO;

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public String addProduct(ProductRequest request) {

        Product product = new Product();

        product.setName(request.getName().trim());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(request.getCategory());
        product.setImageUrl(request.getImageUrl());

        int rowsAffected = productDAO.save(product);

        if (rowsAffected == 1) {
            return "Product added successfully";
        }

        throw new IllegalStateException("Failed to add product");
    }

    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    public Product getProductById(Long id) {
        return productDAO.findById(id);
    }

    public String deleteProduct(Long id) {

        productDAO.findById(id);

        int rowsAffected = productDAO.delete(id);

        if (rowsAffected == 1) {
            return "Product deleted successfully";
        }

        throw new IllegalStateException("Failed to delete product");
    }

    public Product updateProduct(Long id, ProductRequest request) {

        Product existingProduct = productDAO.findById(id);

        existingProduct.setName(request.getName().trim());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setStock(request.getStock());
        existingProduct.setCategory(request.getCategory());
        existingProduct.setImageUrl(request.getImageUrl());

        int rowsAffected = productDAO.update(id, existingProduct);

        if (rowsAffected != 1) {
            throw new IllegalStateException("Failed to update product");
        }

        return productDAO.findById(id);
    }
}