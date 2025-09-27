package com.example.notifier.service.impl;

import com.example.notifier.domain.Channel;
import com.example.notifier.domain.Subscription;
import com.example.notifier.domain.User;
import com.example.notifier.repository.ChannelRepository;
import com.example.notifier.repository.SubscriptionRepository;
import com.example.notifier.repository.UserRepository;
import com.example.notifier.service.api.SubscriptionService;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionServiceImpl(UserRepository userRepository,
                                   ChannelRepository channelRepository,
                                   SubscriptionRepository subscriptionRepository) {
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public void subscribe(Long userId, Long channelId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("Channel not found: " + channelId));

        boolean alreadySubscribed = subscriptionRepository.findByUserId(userId).stream()
                .anyMatch(s -> s.getChannel().getId().equals(channelId));

        if (alreadySubscribed) {
            throw new IllegalStateException("User " + userId + " already subscribed to channel " + channelId);
        }

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setChannel(channel);

        subscriptionRepository.save(subscription);
    }

    @Override
    public void unsubscribe(Long userId, Long channelId) {
        Subscription subscription = subscriptionRepository.findByUserId(userId).stream()
                .filter(s -> s.getChannel().getId().equals(channelId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Subscription not found for user " + userId));

        subscriptionRepository.delete(subscription);
    }
}
