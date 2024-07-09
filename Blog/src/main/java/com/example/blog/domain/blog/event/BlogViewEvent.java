package com.example.blog.domain.blog.event;

import com.example.blog.domain.blog.entity.Blog;
import lombok.Getter;

@Getter
public class BlogViewEvent {
    private final Blog blog;
    private final Long userId;

    public BlogViewEvent(Blog blog, Long userId) {
        this.blog = blog;
        this.userId = userId;
    }
}
