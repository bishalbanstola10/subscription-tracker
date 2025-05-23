package com.bb.auth_service.controller;

import com.bb.auth_service.dto.AuthRequestDTO;
import com.bb.auth_service.dto.AuthResponseDTO;
import com.bb.auth_service.dto.RegisterRequestDTO;
import com.bb.auth_service.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    private RegisterRequestDTO registerRequest;
    private AuthRequestDTO authRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequestDTO();
        registerRequest.setEmail("user@example.com");
        registerRequest.setPassword("password123");

        authRequest = new AuthRequestDTO();
        authRequest.setEmail("user@example.com");
        authRequest.setPassword("password123");
    }

    @Test
    void testRegister_Success() throws Exception {
        when(authService.register(any(RegisterRequestDTO.class))).thenReturn("User created successfully");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("User created successfully"));

        verify(authService).register(any(RegisterRequestDTO.class));
    }
    @Test
    void testLogin_Success() throws Exception {
        AuthResponseDTO authResponse = new AuthResponseDTO();
        authResponse.setEmail("user@example.com");
        authResponse.setUserId("user123");
        authResponse.setToken("jwt-token");

        when(authService.authenticate(any(AuthRequestDTO.class))).thenReturn(Optional.of(authResponse));

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@example.com"))
                .andExpect(jsonPath("$.roles").value("user123"))
                .andExpect(jsonPath("$.token").value("jwt-token"));

        verify(authService).authenticate(any(AuthRequestDTO.class));
    }

    @Test
    void testLogin_InvalidCredentials_ReturnsUnauthorized() throws Exception {
        when(authService.authenticate(any(AuthRequestDTO.class))).thenReturn(Optional.empty());

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isUnauthorized());

        verify(authService).authenticate(any(AuthRequestDTO.class));
    }
    @Test
    void testValidate_ValidToken_ReturnsOk() throws Exception {
        when(authService.validate("valid-token")).thenReturn(true);

        mockMvc.perform(get("/validate")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk());

        verify(authService).validate("valid-token");
    }
    @Test
    void testValidate_InvalidToken_ReturnsUnauthorized() throws Exception {
        when(authService.validate("bad-token")).thenReturn(false);

        mockMvc.perform(get("/validate")
                        .header("Authorization", "Bearer bad-token"))
                .andExpect(status().isUnauthorized());

        verify(authService).validate("bad-token");
    }
    @Test
    void testValidate_MissingHeader_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/validate"))
                .andExpect(status().isUnauthorized());

        verify(authService, never()).validate(any());
    }





}
