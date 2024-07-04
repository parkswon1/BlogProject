package com.example.blog.auth.controller;

import com.example.blog.auth.dto.LoginRequest;
import com.example.blog.auth.dto.RegisterRequest;
import com.example.blog.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Validated @RequestBody RegisterRequest registerRequest) {
        Map<String, String> tokens = authService.register(registerRequest);
        return ResponseEntity.ok(tokens);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Validated @RequestBody LoginRequest loginRequest) {
        Map<String, String> tokens = authService.login(loginRequest);
        return ResponseEntity.ok(tokens);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(Authentication authentication) {
        authService.logout(authentication);
        return ResponseEntity.ok().build();
    }

    // 리프레시 토큰을 사용한 액세스 토큰 재발급
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshAccessToken(@RequestHeader("Authorization") String token) {
        String refreshToken = token.replace("Bearer ", "");
        Map<String, String> tokens = authService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(tokens);
    }
}