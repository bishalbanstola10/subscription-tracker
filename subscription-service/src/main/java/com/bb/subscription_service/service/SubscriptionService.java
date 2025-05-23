package com.bb.subscription_service.service;

import com.bb.subscription_service.DTO.SubscriptionRequestDTO;
import com.bb.subscription_service.DTO.SubscriptionResponseDTO;
import com.bb.subscription_service.kafka.KafkaProducer;
import com.bb.subscription_service.model.Subscription;
import com.bb.subscription_service.repository.SubscriptionRepository;
import com.bb.subscription_service.exception.SubscriptionNotFoundException;
import com.bb.subscription_service.mapper.SubscriptionMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {

    private final SubscriptionRepository repository;
    private final KafkaProducer kafkaProducer;
    public SubscriptionService(SubscriptionRepository repository,KafkaProducer kafkaProducer){
        this.repository=repository;
        this.kafkaProducer=kafkaProducer;
    }

    public List<SubscriptionResponseDTO> getAllUserSubscriptions(String userId){
        return (repository.findByUserId(userId).stream().map(SubscriptionMapper::toDTO).collect(Collectors.toList()));
    }

    public SubscriptionResponseDTO createSubscription(SubscriptionRequestDTO subscriptionRequestDTO){
        Subscription subscription=repository.save(SubscriptionMapper.toModel((subscriptionRequestDTO)));
        kafkaProducer.sendEvent(subscription);
        return SubscriptionMapper.toDTO(subscription);
    }

    public SubscriptionResponseDTO updateSubscription(UUID id,SubscriptionRequestDTO subscriptionRequestDTO){
        Subscription subscription=repository.findById(id).orElseThrow(
                ()->new SubscriptionNotFoundException("Subscription not found with id"+id));
        subscription.setServiceName(subscriptionRequestDTO.getServiceName());
        subscription.setPrice(subscriptionRequestDTO.getPrice());
        subscription.setRecurrence(subscriptionRequestDTO.getRecurrence());
        subscription.setRenewalDate(LocalDate.parse(subscriptionRequestDTO.getRenewalDate()));
        subscription.setUserId(subscription.getUserId());
        Subscription updatedSubscription=repository.save(subscription);
        kafkaProducer.sendEvent(updatedSubscription);
        return SubscriptionMapper.toDTO(updatedSubscription);
    }
    public void deleteSubscription(UUID id){
        repository.deleteById(id);
        return;
    }

}
