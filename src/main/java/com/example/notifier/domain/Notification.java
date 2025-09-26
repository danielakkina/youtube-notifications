package com.example.notifier.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    private Instant createdAt;
    private boolean delivered;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public boolean isDelivered() { return delivered; }
    public void setDelivered(boolean delivered) { this.delivered = delivered; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
