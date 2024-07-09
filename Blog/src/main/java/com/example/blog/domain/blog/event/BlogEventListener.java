package com.example.blog.domain.blog.event;

import com.example.blog.domain.blog.repository.BlogRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BlogEventListener {
    private final BlogRepository blogRepository;

    @EventListener
    public void handleBlogViewEvent(BlogViewEvent event){
        if (!event.getBlog().getUser().getId().equals(event.getUserId())) {
            event.getBlog().setViews(event.getBlog().getViews() + 1);
            blogRepository.save(event.getBlog());
        }
    }
}