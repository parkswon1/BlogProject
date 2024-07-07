package com.example.blog.domain.user.exception;

import com.example.blog.common.exception.auth.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalUserExceptionHandler {
    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<String> PasswordMismatchException(CustomException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(SameAsOldPasswordException.class)
    public ResponseEntity<String> SameAsOldPasswordException(CustomException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}