package com.bb.subscription_service.controller;

import com.bb.subscription_service.DTO.SubscriptionRequestDTO;
import com.bb.subscription_service.DTO.SubscriptionResponseDTO;
import com.bb.subscription_service.service.SubscriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(SubscriptionController.class)
public class SubscriptionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubscriptionService subscriptionService;

    @Autowired
    private ObjectMapper objectMapper;

    private SubscriptionResponseDTO sampleResponseDTO;

    @BeforeEach
    void setup() {
        sampleResponseDTO = new SubscriptionResponseDTO();
        sampleResponseDTO.setServiceName("Netflix");
        sampleResponseDTO.setPrice(15.99);
        sampleResponseDTO.setRecurrence("Monthly");
        sampleResponseDTO.setRenewalDate("2025-01-01");
        sampleResponseDTO.setUserId("user123");
    }
    @Test
    void testGetAllUserSubscriptions() throws Exception {
        List<SubscriptionResponseDTO> subscriptions = Arrays.asList(sampleResponseDTO);

        when(subscriptionService.getAllUserSubscriptions("user123")).thenReturn(subscriptions);

        mockMvc.perform(get("/subscriptions/user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].serviceName").value("Netflix"))
                .andExpect(jsonPath("$[0].price").value(15.99))
                .andExpect(jsonPath("$[0].recurrence").value("Monthly"))
                .andExpect(jsonPath("$[0].renewalDate").value("2025-01-01"))
                .andExpect(jsonPath("$[0].userId").value("user123"));
    }

    @Test
    void testCreateSubscription() throws Exception {
        // Given input request
        SubscriptionRequestDTO requestDTO = new SubscriptionRequestDTO();
        requestDTO.setServiceName("Disney+");
        requestDTO.setPrice(12.99);
        requestDTO.setRecurrence("Monthly");
        requestDTO.setRenewalDate("2025-01-10");
        requestDTO.setUserId("user123");

        // Mocked response from service
        SubscriptionResponseDTO responseDTO = new SubscriptionResponseDTO();
        responseDTO.setServiceName("Disney+");
        responseDTO.setPrice(12.99);
        responseDTO.setRecurrence("Monthly");
        responseDTO.setRenewalDate("2025-01-10");
        responseDTO.setUserId("user123");

        when(subscriptionService.createSubscription(any(SubscriptionRequestDTO.class))).thenReturn(responseDTO);

        // When + Then
        mockMvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serviceName").value("Disney+"))
                .andExpect(jsonPath("$.price").value(12.99))
                .andExpect(jsonPath("$.recurrence").value("Monthly"))
                .andExpect(jsonPath("$.renewalDate").value("2025-01-10"))
                .andExpect(jsonPath("$.userId").value("user123"));

        verify(subscriptionService, times(1)).createSubscription(any(SubscriptionRequestDTO.class));
    }
    @Test
    void testUpdateSubscription() throws Exception {
        // Given
        UUID subscriptionId = UUID.randomUUID();
        SubscriptionRequestDTO requestDTO = new SubscriptionRequestDTO();
        requestDTO.setServiceName("Spotify");
        requestDTO.setPrice(8.99);
        requestDTO.setRecurrence("Monthly");
        requestDTO.setRenewalDate("2025-02-01");
        requestDTO.setUserId("user123");

        SubscriptionResponseDTO responseDTO = new SubscriptionResponseDTO();
        responseDTO.setServiceName("Spotify");
        responseDTO.setPrice(8.99);
        responseDTO.setRecurrence("Monthly");
        responseDTO.setRenewalDate("2025-02-01");
        responseDTO.setUserId("user123");

        when(subscriptionService.updateSubscription(eq(subscriptionId), any(SubscriptionRequestDTO.class)))
                .thenReturn(responseDTO);

        // When + Then
        mockMvc.perform(put("/subscriptions/{id}", subscriptionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serviceName").value("Spotify"))
                .andExpect(jsonPath("$.price").value(8.99))
                .andExpect(jsonPath("$.recurrence").value("Monthly"))
                .andExpect(jsonPath("$.renewalDate").value("2025-02-01"))
                .andExpect(jsonPath("$.userId").value("user123"));

        verify(subscriptionService).updateSubscription(eq(subscriptionId), any(SubscriptionRequestDTO.class));
    }
    @Test
    void testDeleteSubscription() throws Exception {
        // Given
        UUID subscriptionId = UUID.randomUUID();

        // When + Then
        mockMvc.perform(delete("/subscriptions/{id}", subscriptionId))
                .andExpect(status().isNoContent());

        verify(subscriptionService, times(1)).deleteSubscription(subscriptionId);
    }



}
