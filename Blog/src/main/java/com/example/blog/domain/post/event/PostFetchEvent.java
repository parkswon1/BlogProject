package com.example.blog.domain.post.event;

import com.example.blog.domain.post.entity.Post;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Setter
@Getter
public class PostFetchEvent extends ApplicationEvent {
    private final Long postId;
    private Post post;

    public PostFetchEvent(Object source, Long postId) {
        super(source);
        this.postId = postId;
    }
}
