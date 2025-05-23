package com.bb.subscription_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    private String userId;

    @NotNull
    private String serviceName;

    private Double price;

    @NotNull
    private LocalDate renewalDate;

    @NotNull
    private String recurrence;

    public UUID getId() {
        return id;
    }

    public @NotNull String getUserId() {
        return userId;
    }

    public @NotNull String getServiceName() {
        return serviceName;
    }

    public Double getPrice() {
        return price;
    }

    public @NotNull LocalDate getRenewalDate() {
        return renewalDate;
    }

    public @NotNull String getRecurrence() {
        return recurrence;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUserId(@NotNull String userId) {
        this.userId = userId;
    }

    public void setServiceName(@NotNull String serviceName) {
        this.serviceName = serviceName;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setRenewalDate(@NotNull LocalDate renewalDate) {
        this.renewalDate = renewalDate;
    }

    public void setRecurrence(@NotNull String recurrence) {
        this.recurrence = recurrence;
    }
}
