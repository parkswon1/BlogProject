package com.example.blog.auth.security;

import com.example.blog.common.exception.auth.AccessTokenExpiredException;
import com.example.blog.domain.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    private final UserRepository userRepository;

    @Value("${JWT_SECRET_ACCESS}")
    private String accessTokenSecretBase64;

    @Value("${JWT_SECRET_REFRESH}")
    private String refreshTokenSecretBase64;

    @Value("${JWT_ACCESS_EXPIRATION}")
    private long accessTokenExpiration;

    @Value("${JWT_REFRESH_EXPIRATION}")
    private long refreshTokenExpiration;

    private SecretKey accessTokenSecretKey;
    private SecretKey refreshTokenSecretKey;

    @PostConstruct
    public void init() {
        accessTokenSecretKey = decodeSecretKey(accessTokenSecretBase64);
        refreshTokenSecretKey = decodeSecretKey(refreshTokenSecretBase64);
    }

    private SecretKey decodeSecretKey(String base64Key) {
        byte[] decodedKey = Base64.getDecoder().decode(base64Key);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    // 액세스 토큰 생성
    public String generateAccessToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userRepository.findIdByUsername(userDetails.getUsername()));
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(accessTokenSecretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    // 리프레시 토큰 생성
    public String generateRefreshToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(refreshTokenSecretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    // JWT 토큰에서 사용자 이름 추출
    public String extractUsername(String token, boolean isAccessToken) {
        return extractClaim(token, Claims::getSubject, isAccessToken);
    }

    // JWT 토큰에서 특정 클레임 추출
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver, boolean isAccessToken) {
        final Claims claims = extractAllClaims(token, isAccessToken);
        return claimsResolver.apply(claims);
    }

    // JWT 토큰의 모든 클레임 추출
    private Claims extractAllClaims(String token, boolean isAccessToken) {
        return Jwts.parserBuilder()
                .setSigningKey(isAccessToken ? accessTokenSecretKey : refreshTokenSecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // JWT 토큰 검증
    public boolean validateToken(String token, UserDetails userDetails, boolean isAccessToken) {
        final String username = extractUsername(token, isAccessToken);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token, isAccessToken));
    }

    // JWT 토큰 만료 여부 확인
    private boolean isTokenExpired(String token, boolean isAccessToken) {
        boolean isExpired = extractExpiration(token, isAccessToken).before(new Date());
        if (isExpired){
            throw new AccessTokenExpiredException("만료된 토큰입니다.");
        } else{
            return false;
        }
    }

    // JWT 토큰의 만료 시간 추출
    private Date extractExpiration(String token, boolean isAccessToken) {
        return extractClaim(token, Claims::getExpiration, isAccessToken);
    }
}