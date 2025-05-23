package com.bb.subscription_service.integration;

import com.bb.subscription_service.DTO.SubscriptionRequestDTO;
import com.bb.subscription_service.DTO.SubscriptionResponseDTO;
import com.bb.subscription_service.repository.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class IntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private SubscriptionRepository repository;

    @BeforeEach
    void cleanDb() {
        repository.deleteAll();
    }

    @Test
    void testCreateAndFetchSubscription() {
        SubscriptionRequestDTO requestDTO = new SubscriptionRequestDTO();
        requestDTO.setServiceName("YouTube Premium");
        requestDTO.setPrice(11.99);
        requestDTO.setRecurrence("Monthly");
        requestDTO.setRenewalDate("2025-04-01");
        requestDTO.setUserId("user456");

        // Create subscription
        SubscriptionResponseDTO response = webTestClient.post()
                .uri("/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(SubscriptionResponseDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(response);
        assertEquals("YouTube Premium", response.getServiceName());

        // Fetch subscriptions by user
        List<SubscriptionResponseDTO> responseList = webTestClient.get()
                .uri("/subscriptions/user456")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(SubscriptionResponseDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(responseList);
        assertEquals(1, responseList.size());
        assertEquals("YouTube Premium", responseList.get(0).getServiceName());
    }
}
