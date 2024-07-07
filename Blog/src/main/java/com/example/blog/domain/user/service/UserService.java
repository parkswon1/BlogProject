package com.example.blog.domain.user.service;

import com.example.blog.domain.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    User updateUser(Long userId, String name);
    User getUesr(Long userId);
    User getUserProfile(Long userId);
    User saveUserProfile(Long userId, MultipartFile imageFile);
    void changePassword(Long userId, String newPassword);
}
