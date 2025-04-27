package com.example.rbac.mapper;

import com.example.rbac.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<Category> findAll();
    void insertCategory(Category category);
    void updateCategory(Category category);
    void deleteCategory(Long id);
}