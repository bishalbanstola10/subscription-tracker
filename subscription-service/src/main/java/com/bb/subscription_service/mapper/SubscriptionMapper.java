package com.bb.subscription_service.mapper;

import com.bb.subscription_service.DTO.SubscriptionRequestDTO;
import com.bb.subscription_service.DTO.SubscriptionResponseDTO;
import com.bb.subscription_service.model.Subscription;

import java.time.LocalDate;

public class SubscriptionMapper {
    public static SubscriptionResponseDTO toDTO(Subscription subscription){
        SubscriptionResponseDTO responseDTO=new SubscriptionResponseDTO();
        responseDTO.setId(subscription.getId().toString());
        responseDTO.setUserId(subscription.getUserId());
        responseDTO.setPrice(subscription.getPrice());
        responseDTO.setServiceName(subscription.getServiceName());
        responseDTO.setRecurrence(subscription.getRecurrence());
        responseDTO.setRenewalDate(subscription.getRenewalDate().toString());
        return responseDTO;
    }

    public static Subscription toModel(SubscriptionRequestDTO requestDTO){
        Subscription subscription=new Subscription();
        subscription.setUserId(requestDTO.getUserId());
        subscription.setPrice(requestDTO.getPrice());
        subscription.setRenewalDate(LocalDate.parse(requestDTO.getRenewalDate()));
        subscription.setServiceName(requestDTO.getServiceName());
        subscription.setRecurrence(requestDTO.getRecurrence());
        return subscription;
    }
}
