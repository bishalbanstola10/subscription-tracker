package com.bb.subscription_service.exception;

public class SubscriptionNotFoundException extends RuntimeException{
    public SubscriptionNotFoundException(String message) {
        super(message);
    }
}
