package com.example.todoapi.controller;

import com.example.todoapi.dto.ApiResult;
import com.example.todoapi.dto.auth.LoginRequest;
import com.example.todoapi.dto.auth.LoginResponse;
import com.example.todoapi.dto.auth.RegisterRequest;
import com.example.todoapi.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.todoapi.config.SecurityConfig;
import com.example.todoapi.security.JwtAuthenticationFilter;
import com.example.todoapi.security.JwtTokenUtil;

@WebMvcTest(controllers = AuthController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                SecurityConfig.class,
                JwtAuthenticationFilter.class,
                JwtTokenUtil.class
        })
})
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register_shouldReturnSuccessResponse() throws Exception {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setUsername("john_doe");
        request.setPassword("password123");
        request.setEmail("john@example.com");

        LoginResponse loginResponse = new LoginResponse("mocked-jwt-token", "Bearer", "john_doe");
        Mockito.when(userService.register(any(RegisterRequest.class))).thenReturn(loginResponse);

        // Act
        ResultActions result = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.token").value("mocked-jwt-token"))
                .andExpect(jsonPath("$.data.type").value("Bearer"))
                .andExpect(jsonPath("$.data.username").value("john_doe"));
    }

    @Test
    void register_shouldReturnBadRequestResponse() throws Exception {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setUsername("john_doe");
        request.setPassword("password123");
        request.setEmail("john@example.com");

        Mockito.when(userService.register(any(RegisterRequest.class))).thenThrow(new RuntimeException("Username is already taken"));

        // Act
        ResultActions result = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // Assert
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Username is already taken"));
                
    }

    @Test
    void login_shouldReturnSuccessResponse() throws Exception {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setUsername("john_doe");
        request.setPassword("password123");

        LoginResponse loginResponse = new LoginResponse("mocked-jwt-token", "Bearer", "john_doe");
        Mockito.when(userService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        // Act
        ResultActions result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.token").value("mocked-jwt-token"))
                .andExpect(jsonPath("$.data.type").value("Bearer"))
                .andExpect(jsonPath("$.data.username").value("john_doe"));
    }

    @Test
    void login_shouldReturnFailedCredentialsResponse() throws Exception {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setUsername("john_doe");
        request.setPassword("password123");

        Mockito.when(userService.login(any(LoginRequest.class))).thenThrow(new BadCredentialsException("Invalid credentials"));

        // Act
        ResultActions result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    
        // Assert
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value("Invalid username or password"));
    }

    @Test
    void login_shouldReturnFailedResponse() throws Exception {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setUsername("john_doe");
        request.setPassword("password123");

        Mockito.when(userService.login(any(LoginRequest.class))).thenThrow(new RuntimeException("Login failed"));

        // Act
        ResultActions result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    
        // Assert
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Login failed"));
    }

} 