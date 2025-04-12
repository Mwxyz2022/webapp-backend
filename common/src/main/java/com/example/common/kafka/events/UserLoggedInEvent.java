package com.example.common.kafka.events;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class UserLoggedInEvent {

    @JsonProperty("telegramUserId")
    private String telegramUserId;

    @JsonProperty("timestamp")
    private Instant timestamp;

    @JsonProperty("source")
    private String source;

    public UserLoggedInEvent() {
    }

    public UserLoggedInEvent(String telegramUserId, Instant timestamp, String source) {
        this.telegramUserId = telegramUserId;
        this.timestamp = timestamp;
        this.source = source;
    }

    public String getTelegramUserId() {
        return telegramUserId;
    }

    public void setTelegramUserId(String telegramUserId) {
        this.telegramUserId = telegramUserId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "UserLoggedInEvent{" +
                "telegramUserId='" + telegramUserId + '\'' +
                ", timestamp=" + timestamp +
                ", source='" + source + '\'' +
                '}';
    }
}
