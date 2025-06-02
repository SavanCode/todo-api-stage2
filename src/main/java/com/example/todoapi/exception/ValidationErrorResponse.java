package com.example.todoapi.exception;

import lombok.Getter;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 验证错误响应类
 * 继承自ErrorResponse，用于处理参数验证失败的情况
 * 包含具体的字段验证错误信息
 */
@Getter
public class ValidationErrorResponse extends ErrorResponse {
    /**
     * 字段验证错误映射
     * key: 字段名
     * value: 错误消息
     */
    private final Map<String, String> errors;

    /**
     * 构造函数
     * @param status HTTP状态码
     * @param message 错误消息
     * @param errors 字段验证错误映射
     * @param timestamp 错误发生时间
     */
    public ValidationErrorResponse(int status, String message, Map<String, String> errors, LocalDateTime timestamp) {
        super(status, message, timestamp);
        this.errors = errors;
    }
} 