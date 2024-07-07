package com.example.blog.domain.user.Controller;

import com.example.blog.auth.security.CustomUserDetails;
import com.example.blog.domain.user.entity.User;
import com.example.blog.domain.user.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;

    //사용자 정보 업데이트 api
    //지금은 사용자 이름만 변경하지만 나중에 다른 정보도 변경할수 있으니 Body로 받아옴
    @PostMapping("/profile")
    public ResponseEntity<String> updateUesr(Authentication authentication,
                                             @RequestBody Map<String, Object> requestData){
        Long userId = getUserIdFromAuthentication(authentication);
        try {
            userService.updateUser(userId, requestData.get("name").toString());
        }catch (IllegalArgumentException | IllegalAccessException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("사용자 정보가 업데이트 됐습니다.");
    }

    //사용자 정보 받아오는 api
    //사용자 프로필 사진이 있다면 프로필 사진url도 같이 보내줌
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserAndProfile(@PathVariable Long id) {
        User user;
        try {
            user = userService.getUserWithOutPassword(id);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(user);
    }

    //비밀번호 변경 api
    //비밀번호 변경이 성공하면 나중에 클라이언트 측에서 토큰 삭제 요청을 만들어야함
    @PostMapping("/password")
    public ResponseEntity<String> updatePassword(Authentication authentication,
                                                 @RequestBody Map<String, Object> requestData){
        Long userId = getUserIdFromAuthentication(authentication);
        try {
            userService.changePassword(userId, requestData.get("newPassword").toString(), requestData.get("checkPassword").toString());
        }catch (IllegalArgumentException | IllegalAccessException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("사용자 비밀번호가 변경되었습니다.");
    }

    //사용자 프로필 사진 변경 api
    @PostMapping("/image")
    public ResponseEntity<String> updateProfile(Authentication authentication,
                                                @RequestBody MultipartFile imageFile){
        Long userId = getUserIdFromAuthentication(authentication);
        try{
            userService.saveUserProfile(userId, imageFile);
        }catch (IllegalArgumentException | IllegalAccessException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("사용자 프로필이 업데이트 됐습니다.");
    }

    //사용자 프로필 사진만 가져오는 api
    @GetMapping("/image/{id}")
    public ResponseEntity<String> getUserProfileImage(@PathVariable Long id){
        String imageUrl;
        try {
            imageUrl = userService.getUserProfile(id);
        }catch (IllegalArgumentException | IllegalAccessException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(imageUrl);
    }

    //user detail로 부터 사용자 id받아오는 로직
    private static Long getUserIdFromAuthentication(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUserId();
    }
}
