package com.example.blog.auth.controller;

import com.example.blog.auth.dto.LoginRequest;
import com.example.blog.auth.dto.RegisterRequest;
import com.example.blog.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> register(@Validated @RequestBody RegisterRequest registerRequest) {
        try {
            Map<String, String> tokens = authService.register(registerRequest);
            return ResponseEntity.ok(tokens);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequest loginRequest) {
        try {
            Map<String, Object> response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(Authentication authentication) {
        try {
            authService.logout(authentication);
            return ResponseEntity.ok(Map.of("message", "로그아웃 성공"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    // 리프레시 토큰을 사용한 액세스 토큰 재발급
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestHeader("Authorization") String token) {
        try {
            String refreshToken = token.replace("Bearer ", "");
            Map<String, String> tokens = authService.refreshAccessToken(refreshToken);
            return ResponseEntity.ok(tokens);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }
}