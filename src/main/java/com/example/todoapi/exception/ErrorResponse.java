package com.example.todoapi.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 基本错误响应类
 * 用于封装API错误响应的标准格式
 */
@Data
@AllArgsConstructor
public class ErrorResponse {
    /**
     * HTTP状态码
     */
    private int status;

    /**
     * 错误消息
     */
    private String message;

    /**
     * 错误发生的时间戳
     */
    private LocalDateTime timestamp;
} 