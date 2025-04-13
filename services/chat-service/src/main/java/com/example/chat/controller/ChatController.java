package com.example.chat.controller;

import com.example.chat.kafka.ChatKafkaProducer;
import com.example.chat.model.ChatMessage;
import com.example.chat.service.ChatMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ChatKafkaProducer chatKafkaProducer;

    public ChatController(SimpMessagingTemplate messagingTemplate,
                          ChatMessageService chatMessageService,
                          ChatKafkaProducer chatKafkaProducer) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageService = chatMessageService;
        this.chatKafkaProducer = chatKafkaProducer;
    }

    /**
     * Менеджер надсилає повідомлення користувачу (через UI)
     */
    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> sendViaRest(@RequestBody ChatMessage message) {
        log.info("📨 REST: {} -> {}", message.getFrom(), message.getText());

        if (message.getTimestamp() == null) {
            message.setTimestamp(Instant.now());
        }

        // 💾 Зберегти в Mongo
        chatMessageService.save(
                message.getChatId(),
                message.getFrom(),
                message.getTo(),
                message.getText()
        );

        // 🔁 Відправити менеджеру назад у WebSocket (щоб бачив своє повідомлення)
        messagingTemplate.convertAndSend("/topic/chat." + message.getChatId(), message);

        // 📤 Відправити в Kafka → бот → користувач
        chatKafkaProducer.sendToOutgoing(message);

        // ✅ Відповідь
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ok");
        response.put("chatId", message.getChatId());
        response.put("timestamp", message.getTimestamp().toString());

        return ResponseEntity.ok(response);
    }

    /**
     * Якщо хочеш підтримку WebSocket-відправки з фронту (не REST)
     */
    @MessageMapping("/chat")
    public void sendViaWebSocket(ChatMessage message) {
        log.info("💬 WS: {} -> {}", message.getFrom(), message.getText());

        if (message.getTimestamp() == null) {
            message.setTimestamp(Instant.now());
        }

        chatMessageService.save(
                message.getChatId(),
                message.getFrom(),
                message.getTo(),
                message.getText()
        );

        messagingTemplate.convertAndSend("/topic/chat." + message.getChatId(), message);
        chatKafkaProducer.sendToOutgoing(message);
    }

    /**
     * Повертає історію повідомлень по chatId (telegramUserId)
     */
    @GetMapping("/history/{chatId}")
    public List<ChatMessage> getChatHistory(@PathVariable String chatId) {
        log.info("📚 GET chat history for chatId={}", chatId);
        return chatMessageService.getMessages(chatId);
    }
}
