package com.example.blog.domain.like.controller;

import com.example.blog.domain.like.entity.Like;
import com.example.blog.domain.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/post")
    public ResponseEntity<Like> likePost(@RequestParam Long userId, @RequestParam Long postId) throws IllegalAccessException {
        Like like = likeService.likePost(userId, postId);
        return ResponseEntity.ok(like);
    }

    @DeleteMapping("/post")
    public ResponseEntity<String> unlikePost(@RequestParam Long userId, @RequestParam Long postId) {
        likeService.unlikePost(userId, postId);
        return ResponseEntity.ok("Like removed successfully");
    }

    @PostMapping("/blog")
    public ResponseEntity<Like> likeBlog(@RequestParam Long userId, @RequestParam Long blogId) throws IllegalAccessException {
        Like like = likeService.likeBlog(userId, blogId);
        return ResponseEntity.ok(like);
    }

    @DeleteMapping("/blog")
    public ResponseEntity<String> unlikeBlog(@RequestParam Long userId, @RequestParam Long blogId) {
        likeService.unlikeBlog(userId, blogId);
        return ResponseEntity.ok("Like removed successfully");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Like>> allLike(@PathVariable Long userId) {
        List<Like> likes = likeService.allLike(userId);
        return ResponseEntity.ok(likes);
    }
}