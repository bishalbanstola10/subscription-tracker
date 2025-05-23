package com.bb.auth_service.integration;

import com.bb.auth_service.dto.AuthRequestDTO;
import com.bb.auth_service.dto.AuthResponseDTO;
import com.bb.auth_service.dto.RegisterRequestDTO;
import com.bb.auth_service.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AuthIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void cleanDb() {
        userRepository.deleteAll();
    }
    @Test
    void testRegisterAndLoginFlow() {
        // --- Step 1: Register ---
        RegisterRequestDTO registerDTO = new RegisterRequestDTO();
        registerDTO.setEmail("john@example.com");
        registerDTO.setPassword("password123");

        webTestClient.post()
                .uri("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(registerDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("User created successfully");

        // --- Step 2: Login ---
        AuthRequestDTO loginDTO = new AuthRequestDTO();
        loginDTO.setEmail("john@example.com");
        loginDTO.setPassword("password123");

        AuthResponseDTO authResponse = webTestClient.post()
                .uri("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(loginDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AuthResponseDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(authResponse);
        assertEquals("john@example.com", authResponse.getEmail());
        assertNotNull(authResponse.getUserId());
        assertNotNull(authResponse.getToken());

        // --- Step 3: Validate Token ---
        webTestClient.get()
                .uri("/validate")
                .header("Authorization", "Bearer " + authResponse.getToken())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testValidate_InvalidToken() {
        webTestClient.get()
                .uri("/validate")
                .header("Authorization", "Bearer invalid-token")
                .exchange()
                .expectStatus().isUnauthorized();
    }
}
