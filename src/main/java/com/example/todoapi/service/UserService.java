package com.example.todoapi.service;

import com.example.todoapi.dto.auth.LoginRequest;
import com.example.todoapi.dto.auth.LoginResponse;
import com.example.todoapi.dto.auth.RegisterRequest;
import com.example.todoapi.entity.User;

public interface UserService {
    /**
     * 注册新用户
     * @param registerRequest 注册请求
     * @return 注册成功的用户
     */
    User register(RegisterRequest registerRequest);

    /**
     * 用户登录
     * @param loginRequest 登录请求
     * @return 登录响应（包含JWT token）
     */
    LoginResponse login(LoginRequest loginRequest);

    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户实体
     */
    User findByUsername(String username);

    /**
     * 检查用户名是否已存在
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否已存在
     * @param email 邮箱
     * @return 是否存在
     */
    boolean existsByEmail(String email);
} 