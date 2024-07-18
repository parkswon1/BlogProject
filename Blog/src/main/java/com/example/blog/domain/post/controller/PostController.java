package com.example.blog.domain.post.controller;

import com.example.blog.domain.post.dto.PostRequest;
import com.example.blog.domain.post.entity.Post;
import com.example.blog.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @PostMapping("/{userId}")
    public ResponseEntity<Post> createPost(
            @PathVariable Long userId,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam Long categoryId,
            @RequestParam(required = false) List<String> tagNames,
            @RequestParam(required = false) MultipartFile imageFile) {
        try {
            PostRequest postRequest = new PostRequest();
            postRequest.setTitle(title);
            postRequest.setContent(content);
            postRequest.setCategoryId(categoryId);
            postRequest.setTagName(tagNames);
            postRequest.setImageFile(imageFile);

            Post post = postService.createPost(userId, postRequest);
            return ResponseEntity.ok(post);
        } catch (Exception e) {
            logger.error("Error creating post", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable Long postId){
        Post post = postService.getPostById(postId);
        return ResponseEntity.ok(post);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Post> updatePost(@PathVariable Long postId, @RequestBody PostRequest postRequest){
        Post post = postService.updatePost(postId, postRequest);
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId){
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<Post>> getAllPosts(Pageable pageable){
        Page<Post> posts = postService.getAllPosts(pageable);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/blog/{blogId}")
    public ResponseEntity<Page<Post>> getPostsByBlogId(@PathVariable Long blogId, Pageable pageable){
        Page<Post> posts = postService.getPostsByBlogId(blogId, pageable);
        return ResponseEntity.ok(posts);
    }
}