package com.example.todoapi.exception;

/**
 * 未授权异常
 * 用于处理用户认证和权限相关的异常
 */
public class UnauthorizedException extends RuntimeException {
    
    public UnauthorizedException(String message) {
        super(message);
    }
    
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
} 