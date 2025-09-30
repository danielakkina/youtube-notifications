package com.example.notifier.service.impl;

import com.example.notifier.dto.NotificationDto;
import com.example.notifier.dto.VideoDto;
import com.example.notifier.rx.ChannelEventPublisher;
import com.example.notifier.service.api.NotificationService;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final ChannelEventPublisher channelEventPublisher;

    public NotificationServiceImpl(ChannelEventPublisher channelEventPublisher) {
        this.channelEventPublisher = channelEventPublisher;
    }

    @Override
    public Disposable subscribeUser(Long userId, Long channelId, Consumer<NotificationDto> onNext) {
        return channelEventPublisher
                .getStreamForChannel(channelId)
                .map(video -> NotificationDto.fromLive(
                        userId,
                        channelId,
                        "New video uploaded: " + video.title(),
                        video
                ))
                .subscribe(onNext);
    }

}
