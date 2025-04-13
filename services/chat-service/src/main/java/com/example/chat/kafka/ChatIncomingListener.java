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
public class ChatIncomingListener {

    private static final Logger log = LoggerFactory.getLogger(ChatIncomingListener.class);

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatIncomingListener(ChatMessageService chatMessageService,
                                SimpMessagingTemplate messagingTemplate) {
        this.chatMessageService = chatMessageService;
        this.messagingTemplate = messagingTemplate;
    }

    @KafkaListener(topics = "chat-incoming", groupId = "chat-service")
    public void handleIncomingMessage(ChatMessage message) {
        log.info("📥 Отримано повідомлення з Kafka (chat-incoming): {} -> {}", message.getFrom(), message.getText());

        if (message.getTimestamp() == null) {
            message.setTimestamp(Instant.now());
        }

        // 💾 Зберігаємо в Mongo
        chatMessageService.save(
                message.getChatId(),
                message.getFrom(),
                message.getTo(),
                message.getText()
        );

        // 🧠 Тут можеш зробити логіку: визначити менеджера
        // Тимчасово просто шлемо в WebSocket /topic/chat.{chatId}

        messagingTemplate.convertAndSend("/topic/chat." + message.getChatId(), message);
    }
}
