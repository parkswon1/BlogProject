package com.example.blog.domain.post.controller;

import com.example.blog.domain.post.dto.PostRequest;
import com.example.blog.domain.post.entity.Post;
import com.example.blog.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/{userId}")
    public ResponseEntity<Post> createPost(@PathVariable Long userId, @RequestBody PostRequest postRequest){
        Post post = postService.createPost(userId, postRequest);
        return ResponseEntity.ok(post);
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