package com.example.blog.domain.post.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PostViewIncrementEvent extends ApplicationEvent {
    private final Long postId;

    public PostViewIncrementEvent(Object source, Long postId) {
        super(source);
        this.postId = postId;
    }
}
