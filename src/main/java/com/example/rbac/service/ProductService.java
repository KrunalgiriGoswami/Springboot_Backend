package com.example.rbac.service;

import com.example.rbac.entity.Product;
import com.example.rbac.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import java.util.List;

import com.example.rbac.entity.Product;
import com.example.rbac.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductMapper productMapper;

    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public List<Product> getAllProducts() {
        return productMapper.findAll();
    }

    public List<Product> getProductsByCategory(Long categoryId) {
        return productMapper.findByCategoryId(categoryId);
    }

    public List<Product> searchProducts(String query) {
        return productMapper.searchProducts(query);
    }

    public Product getProductById(Long id) {
        return productMapper.findById(id);
    }

    public Product addProduct(Product product) {
        productMapper.insertProduct(product);
        return product;
    }

    public Product updateProduct(Product product) {
        productMapper.updateProduct(product);
        return product;
    }

    public void deleteProduct(Long id) {
        productMapper.deleteProduct(id);
    }
}