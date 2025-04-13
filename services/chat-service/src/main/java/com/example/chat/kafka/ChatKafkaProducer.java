package com.example.chat.kafka;

import com.example.chat.model.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ChatKafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(ChatKafkaProducer.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ChatKafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendToOutgoing(ChatMessage message) {
        log.info("📤 Надсилаємо повідомлення в Kafka (chat-outgoing): {}", message);
        kafkaTemplate.send("chat-outgoing", message);
    }
}
