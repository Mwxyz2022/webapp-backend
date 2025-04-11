package com.example.auth.client;

import com.example.common.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service", url = "http://localhost:8086") // порт user-service
public interface UserClient {

    @GetMapping("/internal/users/{telegramUserId}/exists")
    boolean exists(@PathVariable String telegramUserId);

    @PostMapping("/internal/users")
    void create(@RequestBody UserDto user);

    @GetMapping("/internal/users/{telegramUserId}")
    UserDto getByTelegramUserId(@PathVariable String telegramUserId);
}
