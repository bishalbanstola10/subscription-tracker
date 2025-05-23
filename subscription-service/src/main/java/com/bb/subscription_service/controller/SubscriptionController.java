package com.bb.subscription_service.controller;

import com.bb.subscription_service.DTO.SubscriptionRequestDTO;
import com.bb.subscription_service.DTO.SubscriptionResponseDTO;
import com.bb.subscription_service.DTO.validators.CreateSubscriptionValidationGroup;
import com.bb.subscription_service.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/subscriptions")
@Tag(name = "Subscription",description = "API for managing Subscriptions")
public class SubscriptionController {

    private final SubscriptionService service;

    public SubscriptionController(SubscriptionService service){
        this.service=service;
    }

    @Operation(summary = "Get Subscription For a User")
    @GetMapping("/{userId}")
    public ResponseEntity<List<SubscriptionResponseDTO>> getAllUserSubscriptions(@PathVariable String userId){
        List<SubscriptionResponseDTO> responseDTo= service.getAllUserSubscriptions(userId);
        return ResponseEntity.ok().body(responseDTo);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok().body("hello");
    }

    @Operation(summary = "Create Subscription")
    @PostMapping
    public ResponseEntity<SubscriptionResponseDTO> createSubscription(
            @Validated({Default.class, CreateSubscriptionValidationGroup.class})
            @RequestBody SubscriptionRequestDTO requestDTO){
        SubscriptionResponseDTO responseDTO=service.createSubscription(requestDTO);
        return ResponseEntity.ok().body(responseDTO);
    }

    @Operation(summary = "Update Subscription")
    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionResponseDTO> updateSubscription(
            @Validated({Default.class})
            @RequestBody SubscriptionRequestDTO subscriptionRequestDTO,
            @PathVariable UUID id){
        SubscriptionResponseDTO responseDTO=service.updateSubscription(id,subscriptionRequestDTO);
        return  ResponseEntity.ok().body(responseDTO);
    }

    @Operation(summary = "Delete Subscription")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable  UUID id){
        service.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }


}
