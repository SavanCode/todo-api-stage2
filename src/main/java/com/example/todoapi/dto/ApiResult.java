package com.example.todoapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "API响应包装类")
public class ApiResult<T> {
    
    @Schema(description = "状态码", example = "200")
    private int status;
    
    @Schema(description = "响应消息", example = "操作成功")
    private String message;
    
    @Schema(description = "响应数据")
    private T data;
    
    @Schema(description = "时间戳", example = "2024-01-01T12:00:00")
    private LocalDateTime timestamp;
    
    /**
     * 成功响应
     */
    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(200, "Success", data, LocalDateTime.now());
    }
    
    /**
     * 成功响应（带消息）
     */
    public static <T> ApiResult<T> success(String message, T data) {
        return new ApiResult<>(200, message, data, LocalDateTime.now());
    }
    
    /**
     * 失败响应
     */
    public static <T> ApiResult<T> error(int status, String message) {
        return new ApiResult<>(status, message, null, LocalDateTime.now());
    }
    
    /**
     * 失败响应（带数据）
     */
    public static <T> ApiResult<T> error(int status, String message, T data) {
        return new ApiResult<>(status, message, data, LocalDateTime.now());
    }
} 