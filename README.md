# YouTube Notifier

A simple Spring Boot project that sends real-time video notifications when a channel uploads a new video.  
It uses **RxJava** for publishing events, **Server-Sent Events (SSE)** for live streaming, and **PostgreSQL** for storing data.

---

## Features
- Create channels
- Upload videos
- Subscribe / Unsubscribe users
- Get notifications from database
- Stream live video uploads using SSE

---

## Tech Stack
- Spring Boot
- RxJava 3
- Project Reactor (Flux for SSE)
- PostgreSQL
- JPA/Hibernate

---

## API Endpoints

### Channels
- `POST /channels` → create a channel
- `POST /channels/{channelId}/videos` → upload a video
- `GET /channels/{channelId}/stream` → live video stream (SSE)

### Subscriptions
- `POST /subscriptions/{userId}/{channelId}` → subscribe user
- `DELETE /subscriptions/{userId}/{channelId}` → unsubscribe user

### Notifications
- `GET /notifications/{userId}` → get notifications for a user

---

## Run
1. Configure PostgreSQL in `application.properties`.
2. Start the application:
   ```bash
   ./mvnw spring-boot:run
