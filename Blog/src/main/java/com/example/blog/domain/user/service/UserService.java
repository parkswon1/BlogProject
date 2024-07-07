package com.example.blog.domain.user.service;

import com.example.blog.domain.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    void updateUser(Long userId, String name) throws IllegalAccessException;
    User getUser(Long userId) throws IllegalAccessException;
    String getUserProfile(Long userId) throws IllegalAccessException;
    void saveUserProfile(Long userId, MultipartFile imageFile) throws IllegalAccessException;
    void changePassword(Long userId, String newPassword, String checkPassword) throws IllegalAccessException;
    User getUserWithOutPassword(Long userId) throws IllegalAccessException;
}
