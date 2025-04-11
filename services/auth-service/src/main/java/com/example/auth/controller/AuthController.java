package com.example.auth.controller;

import com.example.auth.service.AuthService;
import com.example.common.telegram.TelegramAuthRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/telegram")
    public String telegram(@RequestBody TelegramAuthRequest request) {
        return authService.telegramAuth(request);
    }
}
