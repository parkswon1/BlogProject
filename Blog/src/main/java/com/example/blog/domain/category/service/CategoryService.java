package com.example.blog.domain.category.service;

import com.example.blog.domain.category.entity.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(String name, Long parentId) throws IllegalAccessException;
    Category getCategoryById(Long id);
    List<Category> getAllCategories();
    List<Category> getSubCategories(Long parentId);
    void deleteCategory(Long id) throws IllegalAccessException;
}