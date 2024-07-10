package com.example.blog.domain.post.event;

import com.example.blog.domain.post.entity.Post;
import com.example.blog.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostEventListener {
    private final PostRepository postRepository;

    @EventListener
    public void handlePostViewIncrementEvent(PostViewIncrementEvent event){
        Post post = postRepository.findById(event.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + event.getPostId()));
        post.setViews(post.getViews() + 1);
        postRepository.save(post);
    }
}
