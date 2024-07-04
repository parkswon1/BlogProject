package com.example.blog.auth.dto;

import lombok.Getter;
import lombok.Setter;

//로그인 할때 필요한 dto
@Setter
@Getter
public class LoginRequest {
    private String username;
    private String password;
}