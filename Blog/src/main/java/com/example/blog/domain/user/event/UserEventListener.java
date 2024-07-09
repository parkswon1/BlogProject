package com.example.blog.domain.user.event;

import com.example.blog.domain.user.entity.User;
import com.example.blog.domain.user.repository.UserRepository;
import com.example.blog.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventListener {
    private final UserService userService;

    @EventListener
    public void handleUserLoadEvent(UserLoadEvent event){
        try{
            User user = userService.getUser(event.getUserId());
            event.setUser(user);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("사용자 불러오기 실패", e);
        }
    }
}
