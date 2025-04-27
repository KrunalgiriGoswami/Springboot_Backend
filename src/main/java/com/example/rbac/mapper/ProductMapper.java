package com.example.rbac.mapper;

import com.example.rbac.entity.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {
    Product findById(Long id);

    List<Product> findAll();

    List<Product> findByCategoryId(Long categoryId);

    List<Product> searchProducts(String query);

    void insertProduct(Product product);

    void updateProduct(Product product);

    void deleteProduct(Long id);
}