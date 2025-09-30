package com.example.notifier.controller;

import com.example.notifier.dto.NotificationDto;
import com.example.notifier.repository.NotificationRepository;
import com.example.notifier.repository.SubscriptionRepository;
import com.example.notifier.service.api.NotificationService;
import io.reactivex.rxjava3.disposables.Disposable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final NotificationService notificationService;

    public NotificationController(NotificationRepository notificationRepository,
                                  SubscriptionRepository subscriptionRepository,
                                  NotificationService notificationService) {
        this.notificationRepository = notificationRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.notificationService = notificationService;
    }

    // Fetch stored notifications (pull model)
    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationDto>> getNotifications(@PathVariable Long userId) {
        List<NotificationDto> notifications = notificationRepository.findAll().stream()
                .filter(n -> n.getUser().getId().equals(userId))
                .map(n -> NotificationDto.fromStored(n.getId(), n.getMessage(), n.isDelivered()))
                .toList();
        return ResponseEntity.ok(notifications);
    }

    // Stream live notifications (push model via SSE)
    @GetMapping(value = "/{userId}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<NotificationDto> streamUserNotifications(@PathVariable Long userId) {
        return Flux.create(sink -> {
            List<Disposable> disposables = subscriptionRepository.findByUserId(userId).stream()
                    .map(subscription -> notificationService.subscribeUser(
                            userId,
                            subscription.getChannel().getId(),
                            sink::next
                    ))
                    .toList();

            sink.onCancel(() -> disposables.forEach(Disposable::dispose));
        }, FluxSink.OverflowStrategy.BUFFER);
    }

}
