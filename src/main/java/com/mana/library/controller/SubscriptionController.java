package com.mana.library.controller;

import com.mana.library.entity.Subscription;
import com.mana.library.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping("/extend")
    public ResponseEntity<Subscription> extendSubscription(
            @RequestParam Long memberId,
            @RequestParam Subscription.SubscriptionType subscriptionType,
            @RequestParam Double amount) {
        try {
            Subscription subscription = subscriptionService.extendSubscription(memberId, subscriptionType, amount);
            return ResponseEntity.ok(subscription);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Subscription>> getMemberSubscriptions(@PathVariable Long memberId) {
        try {
            List<Subscription> subscriptions = subscriptionService.getMemberSubscriptions(memberId);
            return ResponseEntity.ok(subscriptions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
