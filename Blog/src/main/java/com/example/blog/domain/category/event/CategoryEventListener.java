package com.example.blog.domain.category.event;

import com.example.blog.domain.category.entity.Category;
import com.example.blog.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryEventListener {
    private final CategoryRepository categoryRepository;

    @EventListener
    public void handleCategoryFetchEvent(CategoryFetchEvent event){
        Category category = categoryRepository.findById(event.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("카테고리가 없습니다." + event.getCategoryId()));
        event.setCategory(category);
    }
}