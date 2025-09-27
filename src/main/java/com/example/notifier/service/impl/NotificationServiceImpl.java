package com.example.notifier.service.impl;

import com.example.notifier.domain.Notification;
import com.example.notifier.domain.Subscription;
import com.example.notifier.domain.User;
import com.example.notifier.dto.NotificationDto;
import com.example.notifier.dto.VideoDto;
import com.example.notifier.repository.NotificationRepository;
import com.example.notifier.repository.SubscriptionRepository;
import com.example.notifier.repository.UserRepository;
import com.example.notifier.rx.ChannelEventPublisher;
import com.example.notifier.service.api.NotificationService;
import io.reactivex.rxjava3.disposables.Disposable;
import org.springframework.stereotype.Service;


import java.time.Instant;
import io.reactivex.rxjava3.functions.Consumer;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final ChannelEventPublisher eventPublisher;

    public NotificationServiceImpl(SubscriptionRepository subscriptionRepository,
                                   UserRepository userRepository,
                                   NotificationRepository notificationRepository,
                                   ChannelEventPublisher eventPublisher) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Disposable subscribeUser(Long userId, Long channelId, Consumer<NotificationDto> onNext) {
        // Verify user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        // Subscribe to channel’s video stream
        return eventPublisher.getStreamForChannel(channelId)
                .map(video -> createNotification(user, video)) // convert video → notification
                .subscribe(onNext);
    }


    @Override
    public void sendNotification(NotificationDto notificationDto) {
        Notification notification = new Notification();
        notification.setMessage(notificationDto.message());
        notification.setDelivered(notificationDto.delivered());
        notification.setCreatedAt(Instant.now());

        User user = userRepository.findById(notificationDto.id())
                .orElseThrow(() -> new IllegalArgumentException("User not found for notification"));

        notification.setUser(user);
        notificationRepository.save(notification);
    }

    // Helper: build NotificationDto when new video arrives
    private NotificationDto createNotification(User user, VideoDto video) {
        String message = "Hey " + user.getUsername()
                + ", new video uploaded on channel " + video.channelId()
                + ": " + video.title();

        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setCreatedAt(Instant.now());
        notification.setDelivered(true);
        notification.setUser(user);

        notificationRepository.save(notification);

        return new NotificationDto(notification.getId(), notification.getMessage(), notification.isDelivered());
    }
}
