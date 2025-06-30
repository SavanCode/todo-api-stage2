package com.example.todoapi.security;

import com.example.todoapi.entity.User;
import com.example.todoapi.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    private final UserService userService;

    public SecurityUtils(UserService userService) {
        this.userService = userService;
    }

    /**
     * 获取当前认证用户的用户名
     * @return 当前用户名
     */
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        return null;
    }

    /**
     * 获取当前认证用户
     * @return 当前用户实体
     */
    public User getCurrentUser() {
        String username = getCurrentUsername();
        if (username != null) {
            return userService.findByUsername(username);
        }
        return null;
    }

    /**
     * 检查当前用户是否已认证
     * @return 是否已认证
     */
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    /**
     * 检查当前用户是否为指定用户
     * @param username 要检查的用户名
     * @return 是否为指定用户
     */
    public boolean isCurrentUser(String username) {
        String currentUsername = getCurrentUsername();
        return currentUsername != null && currentUsername.equals(username);
    }
} 