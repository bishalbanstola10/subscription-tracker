package com.bb.subscription_service.kafka;

import com.bb.subscription_service.model.Subscription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;
import subscription.events.SubscriptionEvent;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class KafkaProducerTest {
    private KafkaTemplate<String, SubscriptionEvent> kafkaTemplate;
    private KafkaProducer kafkaProducer;

    @BeforeEach
    void setUp() {
        kafkaTemplate = mock(KafkaTemplate.class);
        kafkaProducer = new KafkaProducer(kafkaTemplate);
    }

    @Test
    void testSendEvent() {
        // Given
        Subscription subscription = new Subscription();
        subscription.setId(UUID.randomUUID());
        subscription.setUserId("user123");
        subscription.setServiceName("Netflix");
        subscription.setPrice(15.99);
        subscription.setRenewalDate(LocalDate.of(2025, 1, 1));
        subscription.setRecurrence("Monthly");

        ArgumentCaptor<SubscriptionEvent> eventCaptor = ArgumentCaptor.forClass(SubscriptionEvent.class);

        // When
        kafkaProducer.sendEvent(subscription);

        // Then
        verify(kafkaTemplate).send(eq("subscription"), eventCaptor.capture());

        SubscriptionEvent capturedEvent = eventCaptor.getValue();

        assertEquals("user123", capturedEvent.getUserId());
        assertEquals("Netflix", capturedEvent.getServiceName());
        assertEquals(15.99, capturedEvent.getPrice());
        assertEquals("2025-01-01", capturedEvent.getRenewalDate());
        assertEquals("Monthly", capturedEvent.getRecurrence());
    }
}
