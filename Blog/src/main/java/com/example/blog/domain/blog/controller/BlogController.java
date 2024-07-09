package com.example.blog.domain.blog.controller;

import com.example.blog.auth.security.CustomUserDetails;
import com.example.blog.domain.blog.entity.Blog;
import com.example.blog.domain.blog.service.impl.BlogServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogController {
    private final BlogServiceImpl blogService;

    //블로그 저장 api
    @PostMapping()
    public ResponseEntity<String> saveBlog(Authentication authentication,
                                           @RequestParam String title,
                                           @RequestParam String text,
                                           @RequestPart(value = "file", required = false) MultipartFile file){
        try {
            blogService.saveBlog(getUserIdFromAuthentication(authentication),
                    title,
                    text,
                    file);
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("블로그 저장 완료");
    }

    //블로그id로 블로그를 찾아오는 api
    @GetMapping("/{blogId}")
    public ResponseEntity<Blog> getBlogByBogId(Authentication authentication,
                                               @PathVariable Long blogId){
        try{
            Blog blog = blogService.getBlogByBlogId(blogId, getUserIdFromAuthentication(authentication));
            return ResponseEntity.ok(blog);
        } catch (IllegalAccessException | RuntimeException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    //userid로블로그를 찾아오는 api
    @GetMapping("/user/{userId}")
    public ResponseEntity<Blog> getBlogsList(Authentication authentication,
                                             @PathVariable Long userId){
        try {
            Blog blog = blogService.getBlogByUserId(userId, getUserIdFromAuthentication(authentication));
            return ResponseEntity.ok(blog);
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    //user detail로 부터 사용자 id받아오는 로직
    private static Long getUserIdFromAuthentication(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUserId();
    }
}