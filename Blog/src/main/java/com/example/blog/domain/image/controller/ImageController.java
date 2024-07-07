package com.example.blog.domain.image.controller;

import com.example.blog.domain.image.entity.Image;
import com.example.blog.domain.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {
    @Qualifier("localImageService")
    private final ImageService imageService;

    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam("file")MultipartFile file){
        try{
            Image image = imageService.saveImage(file);
            return ResponseEntity.ok(image);
        } catch (IOException e){
            return ResponseEntity.status(500).body("파일 업로드 중 오류가 발생했습니다.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getImage(@PathVariable Long id){
        try{
            Image image = imageService.findById(id);
            String imageUrl = imageService.getImageUrl(image);
            return ResponseEntity.ok(imageUrl);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
