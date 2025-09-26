package com.example.notifier.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private Instant uploadedAt;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Instant getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(Instant uploadedAt) { this.uploadedAt = uploadedAt; }

    public Channel getChannel() { return channel; }
    public void setChannel(Channel channel) { this.channel = channel; }
}
