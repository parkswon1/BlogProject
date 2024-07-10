package com.example.blog.domain.blog.event;

import com.example.blog.domain.blog.entity.Blog;
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

    @EventListener
    public void handleBlogFetchEvent(BlogFetchEvent event){
        Blog blog = blogRepository.findById(event.getBlogId())
                .orElseThrow(() -> new IllegalArgumentException("블로그를 찾을 수 없습니다. : " + event.getBlogId()));
        event.setBlog(blog);
    }
}