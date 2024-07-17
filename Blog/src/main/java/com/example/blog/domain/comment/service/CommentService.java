package com.example.blog.domain.comment.service;

import com.example.blog.domain.comment.entity.Comment;

import java.util.List;

public interface CommentService {
    Comment createComment(Long postId, Long userId, String content) throws IllegalAccessException;
    void deleteComment(Long commentId);
    List<Comment> getCommentsByPostId(Long postId);
}
