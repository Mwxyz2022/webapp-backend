package com.example.common.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TelegramAuthRequest {

    @JsonProperty("id")
    @NotBlank(message = "Telegram ID is required")
    private String telegramUserId;

    @JsonProperty("username")
    @NotBlank(message = "Username is required")
    private String username;

    @JsonProperty("first_name")
    @Size(max = 100, message = "First name too long")
    private String firstName;

    @JsonProperty("last_name")
    @Size(max = 100, message = "Last name too long")
    private String lastName;

    @JsonProperty("photo_url")
    @Size(max = 512, message = "Photo URL too long")
    private String photoUrl;

    @JsonProperty("auth_date")
    @NotBlank(message = "Auth date is required")
    private String authDate;

    @JsonProperty("hash")
    @NotBlank(message = "Hash is required")
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

    public String getEmail() {
        return username != null ? username + "@telegram.local" : null;
    }
}
