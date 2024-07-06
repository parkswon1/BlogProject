package com.example.blog.auth.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Setter
@Getter
public class RegisterRequest {
    @NotBlank(message = "username이 필요합니다.")
    @Email(message = "username은 이메일 형식이여야 합니다.")
    private String username;

    @NotBlank(message = "별명이 필요합니다.")
    private String name;

    @NotBlank(message = "비밀번호가 있어야 합니다.")
    private String password;
}