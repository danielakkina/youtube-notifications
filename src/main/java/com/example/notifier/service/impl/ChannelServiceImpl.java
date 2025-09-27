package com.example.notifier.service.impl;

import com.example.notifier.domain.Channel;
import com.example.notifier.domain.Video;
import com.example.notifier.dto.*;
import com.example.notifier.repository.ChannelRepository;
import com.example.notifier.repository.VideoRepository;
import com.example.notifier.rx.ChannelEventPublisher;
import com.example.notifier.service.api.ChannelService;
import io.reactivex.rxjava3.core.Flowable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ChannelServiceImpl implements ChannelService {

    private final ChannelRepository channelRepository;
    private final VideoRepository videoRepository;
    private final ChannelEventPublisher eventPublisher;

    public ChannelServiceImpl(ChannelRepository channelRepository,
                              VideoRepository videoRepository,
                              ChannelEventPublisher eventPublisher) {
        this.channelRepository = channelRepository;
        this.videoRepository = videoRepository;
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
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("Channel not found: " + channelId));

        Video video = new Video();
        video.setTitle(dto.title());
        video.setDescription(dto.description());
        video.setUploadedAt(Instant.now());
        video.setChannel(channel);

        Video saved = videoRepository.save(video);

        VideoDto videoDto = new VideoDto(saved.getId(), saved.getTitle(), saved.getDescription(), channelId);

        // Publish video event via RxJava
        eventPublisher.publish(channelId, videoDto);

        return videoDto;
    }

    @Override
    public Flowable<VideoDto> streamVideos(Long channelId) {
        return eventPublisher.getStreamForChannel(channelId);
    }
}
