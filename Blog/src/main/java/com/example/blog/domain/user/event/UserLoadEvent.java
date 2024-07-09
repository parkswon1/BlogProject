package com.example.blog.domain.user.event;

import com.example.blog.domain.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoadEvent {
    private final Long userId;
    private User user;

    public UserLoadEvent(Long userId) {
        this.userId = userId;
    }
}
