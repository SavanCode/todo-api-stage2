package com.example.todoapi.controller;

import com.example.todoapi.dto.ApiResult;
import com.example.todoapi.dto.auth.LoginRequest;
import com.example.todoapi.dto.auth.LoginResponse;
import com.example.todoapi.dto.auth.RegisterRequest;
import com.example.todoapi.entity.User;
import com.example.todoapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "认证管理", description = "用户认证相关接口")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "注册新用户")
    public ResponseEntity<ApiResult<LoginResponse>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            LoginResponse loginResponse = userService.register(registerRequest);
            return ResponseEntity.ok(ApiResult.success(loginResponse));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResult.error(400, e.getMessage()));
        }
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录并获取JWT token")
    public ResponseEntity<ApiResult<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse loginResponse = userService.login(loginRequest);
            return ResponseEntity.ok(ApiResult.success(loginResponse));
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body(ApiResult.error(401, "Invalid username or password"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResult.error(400, e.getMessage()));
        }
    }
} 