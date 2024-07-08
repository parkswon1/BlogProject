package com.example.blog.domain.image.event;

import com.example.blog.domain.image.entity.Image;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.function.Consumer;

@Getter
public class ImageSaveEvent {
    private final MultipartFile file;
    private final Consumer<Image> callback;

    public ImageSaveEvent(MultipartFile file, Consumer<Image> callback) {
        this.file = file;
        this.callback = callback;
    }
}
