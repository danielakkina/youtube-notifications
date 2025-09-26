package com.example.notifier.service.api;

import com.example.notifier.dto.NotificationDto;
import io.reactivex.rxjava3.disposables.Disposable;

import java.util.function.Consumer;

public interface NotificationService {
    // Subscribe user to notification stream
    Disposable subscribeUser(Long userId, Long channelId, Consumer<NotificationDto> onNext);

    // Notify user explicitly (used internally)
    void sendNotification(NotificationDto notificationDto);
}
