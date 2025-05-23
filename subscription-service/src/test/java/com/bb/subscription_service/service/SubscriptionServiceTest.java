package com.bb.subscription_service.service;

import com.bb.subscription_service.DTO.SubscriptionRequestDTO;
import com.bb.subscription_service.DTO.SubscriptionResponseDTO;
import com.bb.subscription_service.exception.SubscriptionNotFoundException;
import com.bb.subscription_service.kafka.KafkaProducer;
import com.bb.subscription_service.model.Subscription;
import com.bb.subscription_service.repository.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SubscriptionServiceTest {
    private SubscriptionRepository subscriptionRepository;
    private SubscriptionService subscriptionService;
    private KafkaProducer kafkaProducer;

    @BeforeEach
    public void setUP(){
        this.subscriptionRepository=mock(SubscriptionRepository.class);
        this.kafkaProducer=mock(KafkaProducer.class);
        this.subscriptionService=new SubscriptionService(subscriptionRepository,kafkaProducer);
    }

    @Test
    public void testGetAllUserSubscriptions() {
        String userId = "user123";
        UUID id1=UUID.randomUUID();
        UUID id2=UUID.randomUUID();
        Subscription sub1 = new Subscription();
        sub1.setId(id1);
        sub1.setUserId(userId);
        sub1.setPrice(20.00);
        sub1.setRecurrence("Monthly");
        sub1.setServiceName("Netflix");
        sub1.setRenewalDate(LocalDate.now().plusDays(20));
        Subscription sub2 = new Subscription();
        sub2.setId(id2);
        sub2.setUserId(userId);
        sub2.setPrice(20.00);
        sub2.setRecurrence("Weekly");
        sub2.setServiceName("Disney");
        sub2.setRenewalDate(LocalDate.now().plusDays(30));

        when(subscriptionRepository.findByUserId(userId)).thenReturn(Arrays.asList(sub1, sub2));

        List<SubscriptionResponseDTO> result = subscriptionService.getAllUserSubscriptions(userId);

        assertEquals(2, result.size());
        assertEquals("Netflix", result.get(0).getServiceName());
        assertEquals("Disney", result.get(1).getServiceName());
        verify(subscriptionRepository, times(1)).findByUserId(userId);

    }
    @Test
    void testCreateSubscription() {
        // Given
        String userId = "user123";
        SubscriptionRequestDTO requestDTO = new SubscriptionRequestDTO();
        requestDTO.setServiceName("Disney+");
        requestDTO.setPrice(12.99);
        requestDTO.setRecurrence("Monthly");
        requestDTO.setRenewalDate(LocalDate.now().plusDays(7).toString());
        requestDTO.setUserId(userId);

        Subscription savedSubscription = new Subscription();
        savedSubscription.setId(UUID.randomUUID());
        savedSubscription.setUserId(userId);
        savedSubscription.setPrice(12.99);
        savedSubscription.setRecurrence("Monthly");
        savedSubscription.setServiceName("Disney+");
        savedSubscription.setRenewalDate(LocalDate.parse(requestDTO.getRenewalDate()));

        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(savedSubscription);

        // When
        SubscriptionResponseDTO responseDTO = subscriptionService.createSubscription(requestDTO);

        // Then
        assertNotNull(responseDTO);
        assertEquals("Disney+", responseDTO.getServiceName());
        assertEquals(12.99, responseDTO.getPrice());
        assertEquals("Monthly", responseDTO.getRecurrence());
        assertEquals(requestDTO.getRenewalDate(), responseDTO.getRenewalDate());
        assertEquals(userId, responseDTO.getUserId());

        verify(subscriptionRepository, times(1)).save(any(Subscription.class));
        verify(kafkaProducer, times(1)).sendEvent(savedSubscription);
    }
    @Test
    void testUpdateSubscription_Success() {
        // Given
        UUID subscriptionId = UUID.randomUUID();
        String userId = "user123";

        // Existing subscription from DB
        Subscription existingSubscription = new Subscription();
        existingSubscription.setId(subscriptionId);
        existingSubscription.setServiceName("Netflix");
        existingSubscription.setPrice(15.99);
        existingSubscription.setRecurrence("Monthly");
        existingSubscription.setRenewalDate(LocalDate.of(2025, 1, 1));
        existingSubscription.setUserId(userId);

        // New update request
        SubscriptionRequestDTO updateRequest = new SubscriptionRequestDTO();
        updateRequest.setServiceName("Hulu");
        updateRequest.setPrice(10.99);
        updateRequest.setRecurrence("Monthly");
        updateRequest.setRenewalDate("2025-01-15");
        updateRequest.setUserId(userId); // this won’t be used in service

        // The updated subscription that repository returns
        Subscription updatedSubscription = new Subscription();
        updatedSubscription.setId(subscriptionId);
        updatedSubscription.setServiceName("Hulu");
        updatedSubscription.setPrice(10.99);
        updatedSubscription.setRecurrence("Monthly");
        updatedSubscription.setRenewalDate(LocalDate.parse(updateRequest.getRenewalDate()));
        updatedSubscription.setUserId(userId);

        when(subscriptionRepository.findById(subscriptionId)).thenReturn(Optional.of(existingSubscription));
        when(subscriptionRepository.save(existingSubscription)).thenReturn(updatedSubscription);

        // When
        SubscriptionResponseDTO responseDTO = subscriptionService.updateSubscription(subscriptionId, updateRequest);

        // Then
        assertNotNull(responseDTO);
        assertEquals("Hulu", responseDTO.getServiceName());
        assertEquals(10.99, responseDTO.getPrice());
        assertEquals("Monthly", responseDTO.getRecurrence());
        assertEquals(updateRequest.getRenewalDate(), responseDTO.getRenewalDate());
        assertEquals(userId, responseDTO.getUserId());

        verify(subscriptionRepository).findById(subscriptionId);
        verify(subscriptionRepository).save(existingSubscription); // same object updated
        verify(kafkaProducer).sendEvent(updatedSubscription);
    }
    @Test
    void testUpdateSubscription_NotFound() {
        // Given
        UUID invalidId = UUID.randomUUID();
        SubscriptionRequestDTO requestDTO = new SubscriptionRequestDTO();
        requestDTO.setServiceName("Prime");
        requestDTO.setPrice(7.99);
        requestDTO.setRecurrence("Monthly");
        requestDTO.setRenewalDate("2025-02-01");
        requestDTO.setUserId("userX");

        when(subscriptionRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Then + When
        assertThrows(SubscriptionNotFoundException.class, () -> {
            subscriptionService.updateSubscription(invalidId, requestDTO);
        });

        verify(subscriptionRepository).findById(invalidId);
        verify(subscriptionRepository, never()).save(any());
        verify(kafkaProducer, never()).sendEvent(any());
    }
    @Test
    void testDeleteSubscription() {
        // Given
        UUID subscriptionId = UUID.randomUUID();

        // When
        subscriptionService.deleteSubscription(subscriptionId);

        // Then
        verify(subscriptionRepository, times(1)).deleteById(subscriptionId);
    }

}
