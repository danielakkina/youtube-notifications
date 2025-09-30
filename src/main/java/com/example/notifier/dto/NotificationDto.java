package com.example.notifier.dto;

public record NotificationDto(
        Long id,            // for DB
        Long userId,        // for live
        Long channelId,     // for live
        String message,
        boolean delivered,  // for DB
        VideoDto video      // for live
) {
    // Factory for live notifications
    public static NotificationDto fromLive(Long userId, Long channelId, String message, VideoDto video) {
        return new NotificationDto(null, userId, channelId, message, false, video);
    }

    // Factory for stored notifications
    public static NotificationDto fromStored(Long id, String message, boolean delivered) {
        return new NotificationDto(id, null, null, message, delivered, null);
    }
}
