package com.example.notifier.controller;

import com.example.notifier.domain.Notification;
import com.example.notifier.dto.NotificationDto;
import com.example.notifier.repository.NotificationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationRepository notificationRepository;

    public NotificationController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationDto>> getNotifications(@PathVariable Long userId) {
        List<NotificationDto> notifications = notificationRepository.findAll().stream()
                .filter(n -> n.getUser().getId().equals(userId))
                .map(n -> new NotificationDto(n.getId(), n.getMessage(), n.isDelivered()))
                .toList();
        return ResponseEntity.ok(notifications);
    }
}
