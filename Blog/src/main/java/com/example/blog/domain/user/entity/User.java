package com.example.blog.domain.user.entity;

import com.example.blog.domain.image.entity.Image;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String username;

    private String name;

    @Column(nullable = false)
    private String password;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

    @Column(nullable = false, updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, insertable = false)
    private LocalDateTime updatedAt;
}