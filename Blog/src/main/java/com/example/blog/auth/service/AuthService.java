package com.example.blog.auth.service;

import com.example.blog.auth.dto.LoginRequest;
import com.example.blog.auth.dto.RegisterRequest;
import com.example.blog.auth.security.JwtTokenUtil;
import com.example.blog.common.exception.auth.CustomException;
import com.example.blog.domain.user.entity.User;
import com.example.blog.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserDetailsService userDetailsService;

    // 회원가입
    public Map<String, String> register(RegisterRequest registerRequest) {
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new CustomException("이미 있는 이메일 입니다.");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setName(registerRequest.getName());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registerRequest.getUsername(), registerRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtTokenUtil.generateAccessToken(authentication);
        String refreshToken = jwtTokenUtil.generateRefreshToken(authentication);
        redisTemplate.opsForValue().set(user.getUsername(), refreshToken);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return tokens;
    }

    // 로그인
    public Map<String, String> login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtTokenUtil.generateAccessToken(authentication);
        String refreshToken = jwtTokenUtil.generateRefreshToken(authentication);
        redisTemplate.opsForValue().set(loginRequest.getUsername(), refreshToken);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return tokens;
    }

    // 로그아웃
    public void logout(Authentication authentication) {
        try{
            if (authentication != null && authentication.isAuthenticated()) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String username = userDetails.getUsername();

                redisTemplate.opsForValue().set(username, "");
            }
        } catch (Exception e) {
            throw new CustomException("로그아웃 실패" + e.getMessage());
        }
    }

    // 리프레시 토큰을 사용한 액세스 토큰, 리프레시 토큰 재발급
    public Map<String, String> refreshAccessToken(String refreshToken) {
        try {
            String username = jwtTokenUtil.extractUsername(refreshToken, false);
            if (username == null) {
                throw new CustomException("토큰에 사용자 email이 없습니다.");
            }

            String storedRefreshToken = (String) redisTemplate.opsForValue().get(username);
            if (Objects.equals(storedRefreshToken, refreshToken)) {
                // UserDetails 객체로 변환
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                String newAccessToken = jwtTokenUtil.generateAccessToken(authentication);
                String newRefreshToken = jwtTokenUtil.generateRefreshToken(authentication);
                redisTemplate.opsForValue().set(username, newRefreshToken);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("accessToken", newAccessToken);
                tokens.put("refreshToken", newRefreshToken);

                return tokens;
            } else {
                throw new CustomException("유효하지 않은 리프레시 토큰입니다.");
            }

        } catch (Exception e) {
            throw new CustomException("토큰 재발급 중 오류가 발생했습니다:");
        }
    }
}