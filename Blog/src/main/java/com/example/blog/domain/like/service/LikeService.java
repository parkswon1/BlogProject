package com.example.blog.domain.like.service;

import com.example.blog.domain.like.entity.Like;

import java.util.List;

public interface LikeService {
    Like likePost(Long userId, Long postId) throws IllegalAccessException;
    void unlikePost(Long userId, Long postId);
    Like likeBlog(Long userId, Long blogId) throws IllegalAccessException;
    void unlikeBlog(Long uesrId, Long blogId);
    Like likeComment(Long userId, Long commentId);
    void unlikeComment(Long userId, Long commentId);
    List<Like> allLike(Long userId);
}
