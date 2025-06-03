package com.example.todoapi.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Schema(description = "Login request data")
public class LoginRequest {
    @NotBlank(message = "Username is required")
    @Schema(description = "Username", example = "john_doe")
    private String username;

    @NotBlank(message = "Password is required")
    @Schema(description = "Password", example = "password123")
    private String password;
} 