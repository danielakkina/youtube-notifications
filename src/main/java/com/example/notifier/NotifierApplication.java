package com.example.notifier;

import com.example.notifier.domain.User;
import com.example.notifier.dto.ChannelDto;
import com.example.notifier.dto.CreateChannelDto;
import com.example.notifier.dto.CreateVideoDto;
import com.example.notifier.repository.UserRepository;
import com.example.notifier.service.api.ChannelService;
import com.example.notifier.service.api.SubscriptionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NotifierApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotifierApplication.class, args);
    }

    // Demo runner
//    @Bean
//    CommandLineRunner demo(ChannelService channelService) {
//        return args -> {
//            // 1. Create channel
//            ChannelDto channel = channelService.createChannel(new CreateChannelDto("TechGuru"));
//
//            // 2. Subscribe to channel stream
//            channelService.streamVideos(channel.id())
//                    .subscribe(video -> System.out.println("Subscriber got video: " + video.title()));
//
//            // 3. Upload a video (should trigger event)
//            channelService.uploadVideo(channel.id(), new CreateVideoDto("Intro to RxJava", "Basics of RxJava"));
//        };
//    }
    @Bean
    CommandLineRunner subscriptionDemo(SubscriptionService subscriptionService,
                                       UserRepository userRepository,
                                       ChannelService channelService) {
        return args -> {
            // 1. Create a user
            User user = new User();
            user.setUsername("Alice");
            user = userRepository.save(user);

            // 2. Create a channel
            ChannelDto channel = channelService.createChannel(new CreateChannelDto("MusicZone"));

            // 3. Subscribe user
            subscriptionService.subscribe(user.getId(), channel.id());
            System.out.println("Alice subscribed to MusicZone");

            // 4. Unsubscribe user
            subscriptionService.unsubscribe(user.getId(), channel.id());
            System.out.println("Alice unsubscribed from MusicZone");
        };
    }

}
