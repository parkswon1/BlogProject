package com.example.blog.domain.post.service;

import com.example.blog.domain.post.dto.PostRequest;
import com.example.blog.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    Post createPost(Long userId, PostRequest postRequest);
    Post getPostById(Long postId);
    Post updatePost(Long postId, PostRequest postRequest);
    void deletePost(Long postId);
    Page<Post> getAllPosts(Pageable pageable);
    Page<Post> getPostsByBlogId(Long blogId, Pageable pageable);
}