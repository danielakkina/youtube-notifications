package com.example.notifier.repository;

import com.example.notifier.domain.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByChannelId(Long channelId);
    List<Subscription> findByUserId(Long userId);
}
