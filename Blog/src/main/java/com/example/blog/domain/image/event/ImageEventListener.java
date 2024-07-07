package com.example.blog.domain.image.event;

import com.example.blog.domain.image.entity.Image;
import com.example.blog.domain.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageEventListener {
    private final ImageService imageService;

    @EventListener
    public void handleImageSaveEvent(ImageSaveEvent event) {
        try {
            Image image = imageService.saveImage(event.getFile());
            event.getCallback().accept(image);
        } catch (Exception e) {
            throw new RuntimeException("image 저장 실패", e);
        }
    }

    @EventListener
    public Image handleImageLoadEvent(ImageLoadEvent event){
        try {
            return imageService.findById(event.getImageId());
        }catch (Exception e){
            throw new RuntimeException("image 불러오기 실패", e);
        }
    }
}
