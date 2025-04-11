package com.example.common.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TelegramAuthRequest {

    @JsonProperty("id")
    private String telegramUserId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("photo_url")
    private String photoUrl;

    @JsonProperty("auth_date")
    private String authDate;

    @JsonProperty("hash")
    private String hash;

    public TelegramAuthRequest() {}

    public String getTelegramUserId() {
        return telegramUserId;
    }

    public void setTelegramUserId(String telegramUserId) {
        this.telegramUserId = telegramUserId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getAuthDate() {
        return authDate;
    }

    public void setAuthDate(String authDate) {
        this.authDate = authDate;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * Telegram повертає email лише для ботів з запитом email.
     * Можна використовувати username як email-замінник.
     */
    public String getEmail() {
        return username != null ? username + "@telegram.local" : null;
    }
}
