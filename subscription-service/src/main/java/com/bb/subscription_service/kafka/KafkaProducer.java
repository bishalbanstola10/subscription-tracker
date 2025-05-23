package com.bb.subscription_service.kafka;

import com.bb.subscription_service.model.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import subscription.events.SubscriptionEvent;

@Service
public class KafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);
    private  final KafkaTemplate<String,SubscriptionEvent> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String,SubscriptionEvent> kafkaTemplate){
        this.kafkaTemplate=kafkaTemplate;
    }
    public void sendEvent(Subscription subscription){
        SubscriptionEvent subscriptionEvent= SubscriptionEvent.newBuilder()
                .setUserId(subscription.getUserId())
                .setServiceName(subscription.getServiceName())
                .setPrice(subscription.getPrice())
                .setRenewalDate(subscription.getRenewalDate().toString())
                .setRecurrence(subscription.getRecurrence())
                .build();
        try{
            kafkaTemplate.send("subscription",subscriptionEvent);
        }
        catch (Exception e){
            log.error("error",e);
        }
    }


}
