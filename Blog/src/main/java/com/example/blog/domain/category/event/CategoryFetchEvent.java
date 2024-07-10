package com.example.blog.domain.category.event;

import com.example.blog.domain.category.entity.Category;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class CategoryFetchEvent extends ApplicationEvent {
    private final Long categoryId;
    private Category category;

    public CategoryFetchEvent(Object source, Long categoryId) {
        super(source);
        this.categoryId = categoryId;
    }
}
