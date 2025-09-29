package com.example.notifier.controller;

import com.example.notifier.dto.*;
import com.example.notifier.service.api.ChannelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/channels")
public class ChannelController {

    private final ChannelService channelService;

    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @PostMapping
    public ResponseEntity<ChannelDto> createChannel(@RequestBody CreateChannelDto dto) {
        return ResponseEntity.ok(channelService.createChannel(dto));
    }

    @PostMapping("/{channelId}/videos")
    public ResponseEntity<VideoDto> uploadVideo(@PathVariable Long channelId,
                                                @RequestBody CreateVideoDto dto) {
        return ResponseEntity.ok(channelService.uploadVideo(channelId, dto));
    }

    @GetMapping(value = "/{channelId}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<VideoDto> streamChannel(@PathVariable Long channelId) {
        // Wrap RxJava Flowable into Reactor Flux for SSE
        return Flux.from(channelService.streamVideos(channelId));
    }
}
