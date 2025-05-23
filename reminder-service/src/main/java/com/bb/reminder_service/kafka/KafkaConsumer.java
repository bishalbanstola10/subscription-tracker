package com.bb.reminder_service.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import subscription.events.SubscriptionEvent;

@Service
public class KafkaConsumer {
    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics="subscription",groupId = "reminder-service")
    public void consumeEvent(SubscriptionEvent subscriptionEvent){
        try {
            // add to db
            // schedule a job every day
            //send email reminders
            log.info("Event received [Subscription Name={}]",subscriptionEvent);
        }
        catch (Exception e){
            log.error("Error deserializing {}", e.getMessage());
        }

    }
}
