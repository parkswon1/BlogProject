package com.example.blog.domain.image.service;

import com.example.blog.domain.image.entity.Image;
import com.example.blog.domain.image.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service("localImageService")
public class LocalImageService implements ImageService{
    private final ImageRepository imageRepository;
    private final String uploadPath;

    public LocalImageService(@Value("${LOCAL_IMAGE_PATH}") String uploadPath, ImageRepository imageRepository){
        this.imageRepository = imageRepository;
        this.uploadPath = uploadPath;
    }


    @Override
    public Image saveImage(MultipartFile file) throws IOException {
        LocalDate now = LocalDate.now();
        String datePath = now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String uploadDir = uploadPath + datePath;
        File dir = new File(uploadDir);

        if (!dir.exists()){
            dir.mkdir();
        }

        String uuid = UUID.randomUUID().toString();
        String originalName = file.getOriginalFilename();
        String saveName = uuid + "_" + originalName;

        Path filePath = Paths.get(uploadDir, saveName);
        Files.copy(file.getInputStream(), filePath);

        String fileUrl = "/images/" + datePath + "/" + saveName;

        Image image = new Image();
        image.setUrl(fileUrl);
        image.setUuid(uuid);
        image.setName(originalName);
        return imageRepository.save(image);
    }

    @Override
    public Image findById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("없는 사진입니다." + id));
    }

    @Override
    public String getImageUrl(Image image) {
        return null;
    }
}
