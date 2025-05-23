package com.bb.subscription_service.DTO;

import com.bb.subscription_service.DTO.validators.CreateSubscriptionValidationGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SubscriptionRequestDTO {


    @NotBlank(message = "UserId is required")
    private String userId;

    @NotBlank(message = "Service Name is required")
    @Size(max=100,message = "Service Name can not be greater than 100 characters")
    private String serviceName;

    private Double price;

    @NotBlank(groups = CreateSubscriptionValidationGroup.class, message = "Renewal Date is required")
    private String renewalDate;

    @NotBlank(message = "Recurrence is required")
    private String recurrence;

    public @NotBlank(message = "UserId is required") String getUserId() {
        return userId;
    }

    public @NotBlank(message = "Service Name is required") @Size(max = 100, message = "Service Name can not be greater than 100 characters") String getServiceName() {
        return serviceName;
    }

    public Double getPrice() {
        return price;
    }

    public @NotBlank(message = "Renewal Date is required") String getRenewalDate() {
        return renewalDate;
    }

    public @NotBlank(message = "Recurrence is required") String getRecurrence() {
        return recurrence;
    }

    public void setUserId(@NotBlank(message = "UserId is required") String userId) {
        this.userId = userId;
    }

    public void setServiceName(@NotBlank(message = "Service Name is required") @Size(max = 100, message = "Service Name can not be greater than 100 characters") String serviceName) {
        this.serviceName = serviceName;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setRenewalDate(@NotBlank(message = "Renewal Date is required") String renewalDate) {
        this.renewalDate = renewalDate;
    }

    public void setRecurrence(@NotBlank(message = "Recurrence is required") String recurrence) {
        this.recurrence = recurrence;
    }
}
