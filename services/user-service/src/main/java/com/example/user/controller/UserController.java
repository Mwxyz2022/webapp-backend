package com.example.user.controller;

import com.example.common.dto.UserDto;
import com.example.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserDto userDto) {
        log.debug("📦 Створення користувача з Telegram ID: {}", userDto.getTelegramUserId());
        userService.createUser(userDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{telegramUserId}/exists")
    public boolean exists(@PathVariable String telegramUserId) {
        log.debug("🔍 Перевірка існування користувача з Telegram ID: {}", telegramUserId);
        return userService.existsByTelegramUserId(telegramUserId);
    }

    @GetMapping("/{telegramUserId}")
    public ResponseEntity<UserDto> getByTelegramUserId(@PathVariable String telegramUserId) {
        log.debug("📥 Отримання користувача з Telegram ID: {}", telegramUserId);
        return ResponseEntity.ok(userService.getByTelegramUserId(telegramUserId));
    }
}
