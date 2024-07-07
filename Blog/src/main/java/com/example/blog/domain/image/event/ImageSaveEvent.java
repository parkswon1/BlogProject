package com.example.blog.domain.image.event;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class ImageSaveEvent {
    private final MultipartFile file;

    public ImageSaveEvent(MultipartFile file){
        this.file = file;
    }
}
