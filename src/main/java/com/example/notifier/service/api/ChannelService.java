package com.example.notifier.service.api;

import com.example.notifier.dto.*;
import io.reactivex.rxjava3.core.Flowable;

public interface ChannelService {
    ChannelDto createChannel(CreateChannelDto dto);
    VideoDto uploadVideo(Long channelId, CreateVideoDto dto);

    // RxJava: observe video upload events for this channel
    Flowable<VideoDto> streamVideos(Long channelId);
}
