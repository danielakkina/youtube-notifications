package com.example.notifier.service.impl;

import com.example.notifier.domain.Channel;
import com.example.notifier.domain.Video;
import com.example.notifier.domain.Subscription;
import com.example.notifier.domain.Notification;
import com.example.notifier.dto.*;
import com.example.notifier.repository.ChannelRepository;
import com.example.notifier.repository.VideoRepository;
import com.example.notifier.repository.SubscriptionRepository;
import com.example.notifier.repository.NotificationRepository;
import com.example.notifier.rx.ChannelEventPublisher;
import com.example.notifier.service.api.ChannelService;
import io.reactivex.rxjava3.core.Flowable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ChannelServiceImpl implements ChannelService {

    private final ChannelRepository channelRepository;
    private final VideoRepository videoRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final NotificationRepository notificationRepository;
    private final ChannelEventPublisher eventPublisher;

    public ChannelServiceImpl(ChannelRepository channelRepository,
                              VideoRepository videoRepository,
                              SubscriptionRepository subscriptionRepository,
                              NotificationRepository notificationRepository,
                              ChannelEventPublisher eventPublisher) {
        this.channelRepository = channelRepository;
        this.videoRepository = videoRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.notificationRepository = notificationRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public ChannelDto createChannel(CreateChannelDto dto) {
        Channel channel = new Channel();
        channel.setName(dto.name());
        Channel saved = channelRepository.save(channel);
        return new ChannelDto(saved.getId(), saved.getName());
    }

    @Override
    public VideoDto uploadVideo(Long channelId, CreateVideoDto dto) {
        // Find channel
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("Channel not found: " + channelId));

        // Save video
        Video video = new Video();
        video.setTitle(dto.title());
        video.setDescription(dto.description());
        video.setUploadedAt(Instant.now());
        video.setChannel(channel);

        Video savedVideo = videoRepository.save(video);

        VideoDto videoDto = new VideoDto(
                savedVideo.getId(),
                savedVideo.getTitle(),
                savedVideo.getDescription(),
                channelId
        );

        // Publish RxJava event (real-time stream)
        eventPublisher.publish(channelId, videoDto);

        //  Notify all subscribers of this channel
        List<Subscription> subs = subscriptionRepository.findByChannelId(channelId);

        for (Subscription sub : subs) {
            Notification notif = new Notification();
            notif.setUser(sub.getUser());
            notif.setMessage("New video uploaded: " + savedVideo.getTitle());
            notif.setDelivered(false);
            notif.setCreatedAt(Instant.now());
            notificationRepository.save(notif);
        }

        return videoDto;
    }

    @Override
    public Flowable<VideoDto> streamVideos(Long channelId) {
        return eventPublisher.getStreamForChannel(channelId);
    }
}
