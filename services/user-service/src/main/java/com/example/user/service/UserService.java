package com.example.user.service;

import com.example.common.dto.UserDto;

public interface UserService {
    boolean existsByTelegramUserId(String telegramUserId);
    void createUser(UserDto dto);
}