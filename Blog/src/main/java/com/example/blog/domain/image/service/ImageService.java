package com.example.blog.domain.image.service;

import com.example.blog.domain.image.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    Image saveImage(MultipartFile file) throws IOException;
    Image findById(Long id);
    String getImageUrl(Image image);
}
