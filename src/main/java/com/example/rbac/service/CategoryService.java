package com.example.rbac.service;

import com.example.rbac.entity.Category;
import com.example.rbac.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    public List<Category> getAllCategories() {
        return categoryMapper.findAll();
    }

    public void addCategory(Category category) {
        categoryMapper.insertCategory(category);
    }

    public void updateCategory(Category category) {
        categoryMapper.updateCategory(category);
    }

    public void deleteCategory(Long id) {
        categoryMapper.deleteCategory(id);
    }
}