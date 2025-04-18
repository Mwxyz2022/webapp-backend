package com.example.common.dto;

import java.io.Serializable;

public class UserDto implements Serializable {

    private Long id;
    private String telegramUserId;
    private String username;
    private String firstName;
    private String lastName;
    private String photoUrl;
    private String role;

    public UserDto() {}

    public UserDto(Long id, String telegramUserId, String username,
                   String firstName, String lastName, String photoUrl, String role) {
        this.id = id;
        this.telegramUserId = telegramUserId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photoUrl = photoUrl;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
