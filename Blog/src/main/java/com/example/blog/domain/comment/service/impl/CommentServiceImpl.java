package com.example.blog.domain.comment.service.impl;

import com.example.blog.domain.comment.entity.Comment;
import com.example.blog.domain.comment.repository.CommentRepository;
import com.example.blog.domain.comment.service.CommentService;
import com.example.blog.domain.post.entity.Post;
import com.example.blog.domain.post.service.PostService;
import com.example.blog.domain.user.entity.User;
import com.example.blog.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final PostService postService;
    private final UserService userService;

    @Override
    public Comment createComment(Long postId, Long userId, String content) throws IllegalAccessException {
        Post post = postService.getPostById(postId);
        User user = userService.getUser(userId);

        if (post == null || user == null) {
            throw new IllegalArgumentException("Invalid postId or userId");
        }

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setPost(post);
        comment.setUser(user);

        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }
}
