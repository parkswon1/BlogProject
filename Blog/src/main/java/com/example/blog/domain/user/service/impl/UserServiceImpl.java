package com.example.blog.domain.user.service.impl;

import com.example.blog.domain.image.event.ImageSaveEvent;
import com.example.blog.domain.user.entity.User;
import com.example.blog.domain.user.exception.PasswordMismatchException;
import com.example.blog.domain.user.exception.SameAsOldPasswordException;
import com.example.blog.domain.user.repository.UserRepository;
import com.example.blog.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    //사용자의 정보를 받아오는 메서드
    @Override
    public User getUser(Long userId) throws IllegalAccessException {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()){
            throw new IllegalAccessException("사용자가 없습니다.");
        }
        return userOpt.get();
    }

    //사용자의 정보를 업데이트하는 메서드 지금은 이름밖에 없지만 나중에 뭔가 더 추가될수도?
    @Override
    public void updateUser(Long userId, String name) throws IllegalAccessException {
        User user = getUser(userId);
        user.setName(name);
        userRepository.save(user);
    }

    //사용자의 프로필 사진을 가져오는 메서드 프로필 없으면 null로 반환 있으면 url로 반환
    @Override
    public String getUserProfile(Long userId) throws IllegalAccessException {
        return getUser(userId).getImage().getUrl();
    }

    //사용자의 프로필을 새로운 사진으로 저장하는 메서드 이벤트 발행을 통해서 사진저장
    @Override
    public void saveUserProfile(Long userId, MultipartFile imageFile) throws IllegalAccessException {
        User user = getUser(userId);

        if (imageFile != null && !imageFile.isEmpty()){
            ImageSaveEvent event = new ImageSaveEvent(imageFile, user::setImage);
            eventPublisher.publishEvent(event);
        }else{
            user.setImage(null);
        }
        userRepository.save(user);
    }

    //사용자의 비밀번호 변경
    @Override
    public void changePassword(Long userId, String newPassword, String checkPassword) throws IllegalAccessException {
        User user = getUser(userId);

        String checkPasswordEncoded = passwordEncoder.encode(checkPassword);
        if (!checkPasswordEncoded.equals(user.getPassword())){
            throw new PasswordMismatchException("사용자 비밀번호가 틀립니다.");
        }

        String newPasswordEncoded = passwordEncoder.encode(newPassword);
        if (newPasswordEncoded.equals(user.getPassword())){
            throw new SameAsOldPasswordException("예전 비밀번호랑 같은 비밀번호로 변경할 수 없습니다.");
        }

        user.setPassword(newPasswordEncoded);
        userRepository.save(user);
    }

    @Override
    public User getUserWithOutPassword(Long userId) throws IllegalAccessException {
        User user = getUser(userId);
        user.setPassword(null);
        return user;
    }
}