package com.example.chat.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "messages")
public class ChatMessageEntity {

    @Id
    private String id;

    private String chatId;      // Наприклад: telegramUserId або інше
    private String from;        // хто надіслав (user / manager ID)
    private String to;          // кому надіслав (user / manager ID)
    private String text;        // текст повідомлення
    private Instant timestamp;  // коли надіслано

    public ChatMessageEntity() {
    }

    public ChatMessageEntity(String chatId, String from, String to, String text, Instant timestamp) {
        this.chatId = chatId;
        this.from = from;
        this.to = to;
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
