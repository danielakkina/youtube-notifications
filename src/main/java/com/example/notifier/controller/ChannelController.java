package com.example.notifier.controller;

import com.example.notifier.dto.*;
import com.example.notifier.service.api.ChannelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
