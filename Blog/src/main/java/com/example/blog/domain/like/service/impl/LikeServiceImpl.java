package com.example.blog.domain.like.service.impl;

import com.example.blog.domain.blog.service.BlogService;
import com.example.blog.domain.comment.service.CommentService;
import com.example.blog.domain.like.entity.Like;
import com.example.blog.domain.like.repository.LikeRepository;
import com.example.blog.domain.like.service.LikeService;
import com.example.blog.domain.post.service.PostService;
import com.example.blog.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;

    private final UserService userService;
    private final PostService postService;
    private final BlogService blogService;

    @Override
    public Like likePost(Long userId, Long postId) throws IllegalAccessException {
        Like like = new Like();
        like.setUser(userService.getUser(userId));
        like.setPost(postService.getPostById(postId));
        return likeRepository.save(like);
    }

    @Override
    public void unlikePost(Long userId, Long postId) {
        likeRepository.findByUserIdAndPostId(userId, postId);
        likeRepository.deleteById(postId);
    }

    @Override
    public Like likeBlog(Long userId, Long blogId) throws IllegalAccessException {
        Like like = new Like();
        like.setUser(userService.getUser(userId));
        like.setBlog(blogService.getBlogById(blogId));
        return likeRepository.save(like);
    }

    @Override
    public void unlikeBlog(Long uesrId, Long blogId) {
        likeRepository.findByUserIdAndBlogId(uesrId, blogId);
        likeRepository.deleteById(blogId);
    }

    @Override
    public Like likeComment(Long userId, Long commentId) {
        return null;
    }

    @Override
    public void unlikeComment(Long userId, Long commentId) {

    }

    @Override
    public List<Like> allLike(Long userId) {
        return likeRepository.findAllLikeByUserId(userId);
    }
}
