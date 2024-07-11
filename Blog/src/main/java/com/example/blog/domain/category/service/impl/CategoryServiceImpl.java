package com.example.blog.domain.category.service.impl;

import com.example.blog.domain.category.entity.Category;
import com.example.blog.domain.category.repository.CategoryRepository;
import com.example.blog.domain.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    @Override
    public Category createCategory(String name, Long parentId) throws IllegalAccessException {
        if (categoryRepository.findByName(name).isPresent()){
            throw new IllegalAccessException("이미 있는 카테고리입니다. " + name);
        }
        Category category = new Category();
        category.setName(name);

        if (parentId != null){
            Category parent = categoryRepository.findById(parentId)
                    .orElseThrow(() -> new IllegalArgumentException("부모 카테고리가 업습니다. " + parentId));
            category.setParent(parent);
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("카테고리가 없습니다." + id));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> getSubCategories(Long parentId) {
        Category parent = getCategoryById(parentId);
        return categoryRepository.findByParent(parent);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("id로 시작하는 카테고리가 없습니다." + categoryId));

        if (!category.getSubCategories().isEmpty()) {
            throw new IllegalArgumentException("하위 카테고리가 있어서 삭제가 불가능합니다.");
        }

        categoryRepository.delete(category);
    }
}
