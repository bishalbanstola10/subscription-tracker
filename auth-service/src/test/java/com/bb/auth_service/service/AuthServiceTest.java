package com.bb.auth_service.service;

import com.bb.auth_service.dto.AuthRequestDTO;
import com.bb.auth_service.dto.AuthResponseDTO;
import com.bb.auth_service.dto.RegisterRequestDTO;
import com.bb.auth_service.exception.UserAlreadyExistsException;
import com.bb.auth_service.model.AppUser;
import com.bb.auth_service.repository.UserRepository;
import com.bb.auth_service.util.JWTUtil;
import io.jsonwebtoken.JwtException;
import org.apache.catalina.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {
    private UserRepository userRepository;
    private JWTUtil jwtUtil;
    private AuthService authService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUP(){
        userRepository=mock(UserRepository.class);
        jwtUtil=mock((JWTUtil.class));
        passwordEncoder=mock(PasswordEncoder.class);
        authService=new AuthService(userRepository,jwtUtil,passwordEncoder);
    }

    @Test
    public void testRegister(){
        RegisterRequestDTO registerRequestDTO=new RegisterRequestDTO();
        registerRequestDTO.setEmail("test@test.com");
        registerRequestDTO.setPassword("hello@123");

        when(userRepository.existsByEmail(registerRequestDTO.getEmail())).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");

        String response= authService.register(registerRequestDTO);
        assertEquals("User created successfully",response);
        verify(userRepository, times(1)).save(any(AppUser.class));
    }

    @Test
    void testRegister_UserAlreadyExists() {
        // Given
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setEmail("existing@example.com");

        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        // Then
        assertThrows(UserAlreadyExistsException.class, () -> {
            authService.register(request);
        });

        verify(userRepository, never()).save(any());
    }
    @Test
    void testAuthenticate_Success() {
        // Given
        UUID userId=UUID.randomUUID();
        AuthRequestDTO request = new AuthRequestDTO();
        request.setEmail("user@example.com");
        request.setPassword("correct_password");

        AppUser user = new AppUser();
        user.setId(userId);
        user.setEmail("user@example.com");
        user.setPassword("encodedPassword");
        user.setRoles(Set.of("USER"));

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("correct_password", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("user@example.com", Set.of("USER"))).thenReturn("mocked-jwt-token");

        // When
        Optional<AuthResponseDTO> responseOpt = authService.authenticate(request);

        // Then
        assertTrue(responseOpt.isPresent());
        AuthResponseDTO response = responseOpt.get();
        assertEquals("user@example.com", response.getEmail());
        assertEquals(userId.toString(), response.getUserId());
        assertEquals("mocked-jwt-token", response.getToken());

        verify(userRepository).findByEmail("user@example.com");
        verify(passwordEncoder).matches("correct_password", "encodedPassword");
        verify(jwtUtil).generateToken("user@example.com", Set.of("USER"));
    }
    @Test
    void testAuthenticate_InvalidPassword() {
        // Given
        AuthRequestDTO request = new AuthRequestDTO();
        request.setEmail("user@example.com");
        request.setPassword("wrong_password");

        AppUser user = new AppUser();
        user.setEmail("user@example.com");
        user.setPassword("encodedPassword");
        user.setRoles(Set.of("USER"));

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong_password", "encodedPassword")).thenReturn(false);

        // When
        Optional<AuthResponseDTO> response = authService.authenticate(request);

        // Then
        assertFalse(response.isPresent());

        verify(userRepository).findByEmail("user@example.com");
        verify(passwordEncoder).matches("wrong_password", "encodedPassword");
        verify(jwtUtil, never()).generateToken(any(), any());
    }
    @Test
    void testAuthenticate_EmailNotFound() {
        // Given
        AuthRequestDTO request = new AuthRequestDTO();
        request.setEmail("nonexistent@example.com");
        request.setPassword("irrelevant");

        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // When
        Optional<AuthResponseDTO> response = authService.authenticate(request);

        // Then
        assertFalse(response.isPresent());

        verify(userRepository).findByEmail("nonexistent@example.com");
        verify(passwordEncoder, never()).matches(any(), any());
        verify(jwtUtil, never()).generateToken(any(), any());
    }
    @Test
    void testValidate_ValidToken_ReturnsTrue() {
        // Given
        String token = "valid.jwt.token";

        // No exception thrown
        doNothing().when(jwtUtil).validateToken(token);

        // When
        boolean result = authService.validate(token);

        // Then
        assertTrue(result);
        verify(jwtUtil).validateToken(token);
    }
    @Test
    void testValidate_InvalidToken_ReturnsFalse() {
        // Given
        String token = "invalid.jwt.token";

        // Simulate JWT exception
        doThrow(new JwtException("Token is invalid")).when(jwtUtil).validateToken(token);

        // When
        boolean result = authService.validate(token);

        // Then
        assertFalse(result);
        verify(jwtUtil).validateToken(token);
    }





}
