//This class is the heart of our pub-sub system.
//It manages a FlowableProcessor for each channel, which emits video events to subscribers.
package com.example.notifier.rx;

import com.example.notifier.dto.VideoDto;
import io.reactivex.rxjava3.processors.FlowableProcessor;
import io.reactivex.rxjava3.processors.PublishProcessor;
import io.reactivex.rxjava3.core.Flowable;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class ChannelEventPublisher {

    private final ConcurrentMap<Long, FlowableProcessor<VideoDto>> processors = new ConcurrentHashMap<>();

    // Get stream of videos for a channel
    public Flowable<VideoDto> getStreamForChannel(Long channelId) {
        return processors
                .computeIfAbsent(channelId, id -> PublishProcessor.<VideoDto>create().toSerialized())
                .onBackpressureBuffer();
    }

    // Publish a new video to subscribers
    public void publish(Long channelId, VideoDto video) {
        FlowableProcessor<VideoDto> processor = processors.get(channelId);
        if (processor != null) {
            processor.onNext(video);
        }
    }
}
