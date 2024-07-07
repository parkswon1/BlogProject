package com.example.blog.domain.image.event;

import lombok.Getter;

@Getter
public class ImageLoadEvent {
    private final Long imageId;

    public ImageLoadEvent(Long imageId){
        this.imageId = imageId;
    }
}
