package com.example.user.controller;

import com.example.common.dto.UserDto;
import com.example.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserDto userDto) {
        userService.createUser(userDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{telegramUserId}/exists")
    public boolean exists(@PathVariable String telegramUserId) {
        return userService.existsByTelegramUserId(telegramUserId);
    }
}
