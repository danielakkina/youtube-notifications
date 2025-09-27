package com.example.notifier.controller;

import com.example.notifier.service.api.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/{userId}/{channelId}")
    public ResponseEntity<String> subscribe(@PathVariable Long userId,
                                            @PathVariable Long channelId) {
        subscriptionService.subscribe(userId, channelId);
        return ResponseEntity.ok("User " + userId + " subscribed to channel " + channelId);
    }

    @DeleteMapping("/{userId}/{channelId}")
    public ResponseEntity<String> unsubscribe(@PathVariable Long userId,
                                              @PathVariable Long channelId) {
        subscriptionService.unsubscribe(userId, channelId);
        return ResponseEntity.ok("User " + userId + " unsubscribed from channel " + channelId);
    }
}
