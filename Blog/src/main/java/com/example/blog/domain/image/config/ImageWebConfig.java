package com.example.blog.domain.image.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ImageWebConfig implements WebMvcConfigurer {
    private final String uploadPath;

    public ImageWebConfig(@Value("${LOCAL_IMAGE_PATH}") String uploadPath){
        this.uploadPath = uploadPath;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadPath);
    }
}