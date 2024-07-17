package com.example.blog.domain.like.repository;

import com.example.blog.domain.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findAllLikeByUserId(Long userId);
    Optional<Like> findByUserIdAndPostId(Long userId, Long postId);
    Optional<Like> findByUserIdAndBlogId(Long userId, Long blogId);
    Optional<Like> findByUserIdAndCommentId(Long userId, Long commentId);
}
