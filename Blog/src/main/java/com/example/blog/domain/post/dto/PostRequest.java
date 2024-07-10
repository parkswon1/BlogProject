package com.example.blog.domain.post.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class PostRequest {
    private String title;
    private String content;
    private Long categoryId;
    private List<String> tagName;
    private MultipartFile imageFile;
}
