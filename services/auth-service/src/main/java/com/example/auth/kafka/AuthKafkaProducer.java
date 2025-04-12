package com.example.auth.kafka;

import com.example.common.kafka.events.UserLoggedInEvent;
import com.example.common.kafka.events.UserLoggedOutEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class AuthKafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(AuthKafkaProducer.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public AuthKafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendLoginEvent(String telegramUserId) {
        UserLoggedInEvent event = new UserLoggedInEvent(
                telegramUserId,
                Instant.now(),
                "auth-service"
        );

        log.info("📤 Надсилання події входу користувача в Kafka: {}", event);
        kafkaTemplate.send("user.logged-in", event);
    }

    public void sendLogoutEvent(String telegramUserId) {
        UserLoggedOutEvent event = new UserLoggedOutEvent(
                telegramUserId,
                Instant.now(),
                "auth-service"
        );

        log.info("📤 Надсилання події виходу користувача в Kafka: {}", event);
        kafkaTemplate.send("user.logged-out", event);
    }
}
