package com.example.auth.controller;

import com.example.auth.service.AuthService;
import com.example.common.dto.RefreshTokenRequest;
import com.example.common.dto.UserDto;
import com.example.common.telegram.TelegramAuthRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/telegram")
    public String telegram(@Valid @RequestBody TelegramAuthRequest request) {
        log.debug("📥 Отримано TelegramAuthRequest: telegramUserId={}, username={}",
                request.getTelegramUserId(), request.getUsername());
        return authService.telegramAuth(request);
    }

    @GetMapping("/me")
    public UserDto me() {
        log.debug("🔎 Отримання поточного користувача через /me");
        return authService.getCurrentUser();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        log.debug("🚪 Вихід користувача через /logout");
        authService.logout();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        log.debug("🔁 Запит на оновлення токена: refreshToken={}", request.getRefreshToken());
        String newAccessToken = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(newAccessToken);
    }
}
