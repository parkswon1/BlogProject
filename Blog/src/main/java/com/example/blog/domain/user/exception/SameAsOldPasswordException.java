package com.example.blog.domain.user.exception;

public class SameAsOldPasswordException extends RuntimeException{
    public SameAsOldPasswordException(String message){
        super(message);
    }
}
