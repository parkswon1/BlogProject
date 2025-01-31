package com.example.blog.auth.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

//로그인 할때 필요한 dto
@Setter
@Getter
public class LoginRequest {
    @NotBlank(message = "username이 필요합니다.")
    @Email(message = "username은 이메일 형식이여야 합니다.")
    private String username;

    @NotBlank(message = "비밀번호가 있어야 합니다.")
    private String password;
}