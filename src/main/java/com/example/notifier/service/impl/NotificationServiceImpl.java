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

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
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
