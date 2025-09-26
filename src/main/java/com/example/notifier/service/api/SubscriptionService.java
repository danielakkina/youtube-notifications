package com.example.notifier.service.api;

public interface SubscriptionService {
    void subscribe(Long userId, Long channelId);
    void unsubscribe(Long userId, Long channelId);
}
