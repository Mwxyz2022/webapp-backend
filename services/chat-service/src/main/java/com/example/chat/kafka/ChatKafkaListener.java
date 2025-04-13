package com.example.chat.kafka;

import com.example.chat.model.ChatMessage;
import com.example.chat.service.ChatMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ChatKafkaListener {

    private static final Logger log = LoggerFactory.getLogger(ChatKafkaListener.class);
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    public ChatKafkaListener(SimpMessagingTemplate messagingTemplate,
                             ChatMessageService chatMessageService) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageService = chatMessageService;
    }

    @KafkaListener(topics = "chat-response", groupId = "chat-service")
    public void listen(ChatMessage message) {
        log.info("📥 Kafka (chat-response): {} -> {}", message.getFrom(), message.getText());

        if (message.getTimestamp() == null) {
            message.setTimestamp(Instant.now());
        }

        // 💾 зберігаємо в Mongo
        chatMessageService.save(
                message.getChatId(),
                message.getFrom(),
                message.getTo(),
                message.getText()
        );

        // 🔁 транслюємо в WebSocket
        messagingTemplate.convertAndSend("/topic/chat." + message.getChatId(), message);
    }
}
