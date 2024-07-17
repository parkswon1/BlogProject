package com.example.blog.domain.blog.repository;

import com.example.blog.domain.blog.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    Optional<Blog> findByUserId(Long userId);

    @Query("SELECT b FROM Blog b LEFT JOIN FETCH b.user LEFT JOIN FETCH b.image")
    List<Blog> findAllWithAssociations();
}
