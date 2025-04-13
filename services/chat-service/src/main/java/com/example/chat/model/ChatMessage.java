package com.example.chat.model;

import java.time.Instant;

public class ChatMessage {

    private String chatId;      // канал, ідентифікатор чату
    private String from;
    private String to;
    private String text;
    private Instant timestamp;

    public ChatMessage() {}

    public ChatMessage(String chatId, String from, String to, String text, Instant timestamp) {
        this.chatId = chatId;
        this.from = from;
        this.to = to;
        this.text = text;
        this.timestamp = timestamp;
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
