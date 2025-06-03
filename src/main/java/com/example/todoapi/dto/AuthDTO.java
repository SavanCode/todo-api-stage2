package com.example.todoapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Schema(description = "Authentication data transfer object")
public class AuthDTO {
    @NotBlank(message = "Username is required")
    @Schema(description = "Username", example = "john_doe")
    private String username;

    @NotBlank(message = "Password is required")
    @Schema(description = "Password", example = "password123")
    private String password;
} 