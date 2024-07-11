package com.example.blog.domain.blog.event;

import com.example.blog.domain.blog.entity.Blog;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class BlogFetchEvent extends ApplicationEvent {
    private final Long userId;
    private Blog blog;

    public BlogFetchEvent(Object source, Long userId) {
        super(source);
        this.userId = userId;
    }
}
