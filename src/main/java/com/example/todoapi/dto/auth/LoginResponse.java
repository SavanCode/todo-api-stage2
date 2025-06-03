package com.example.todoapi.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Login response data")
public class LoginResponse {
    @Schema(description = "JWT token")
    private String token;

    @Schema(description = "Token type")
    private String type = "Bearer";

    @Schema(description = "Username")
    private String username;
} 