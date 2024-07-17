package com.example.blog.domain.blog.service;


import com.example.blog.domain.blog.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface BlogService {
    void saveBlog(Long userid, String title, String text, MultipartFile imageFile);
    Blog getBlogByBlogId(Long blogId, Long viewerId) throws IllegalAccessException;
    Blog getBlogByUserId(Long uesrId, Long viewerId) throws IllegalAccessException;
    Page<Blog> getBlogsList(Pageable pageable) throws IllegalAccessException;
    public Blog getBlogById(Long blogId);
}
