package com.example.blog.domain.blog.controller;

import com.example.blog.domain.blog.entity.Blog;
import com.example.blog.domain.blog.service.impl.BlogServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blogs")
@RequiredArgsConstructor
public class BlogsController {
    private final BlogServiceImpl blogService;
    @GetMapping
    public ResponseEntity<Page<Blog>> getBlogsList(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size){
        try{
            Pageable pageable = PageRequest.of(page, size);
            Page<Blog> blogs = blogService.getBlogsList(pageable);
            return ResponseEntity.ok(blogs);
        }catch (IllegalAccessException e){
            return ResponseEntity.status(404).body(null);
        }
    }
}