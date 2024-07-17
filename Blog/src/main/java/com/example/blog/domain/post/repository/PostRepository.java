package com.example.blog.domain.post.repository;

import com.example.blog.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByBlogId(Long blogId, Pageable pageable);
    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.postTags LEFT JOIN FETCH p.blog LEFT JOIN FETCH p.category LEFT JOIN FETCH p.image")
    List<Post> findAllWithAssociations();
}
