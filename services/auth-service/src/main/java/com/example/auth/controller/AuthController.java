package com.example.auth.controller;

import com.example.auth.service.AuthService;
import com.example.common.dto.RefreshTokenRequest;
import com.example.common.dto.UserDto;
import com.example.common.telegram.TelegramAuthRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/telegram")
    public String telegram(@Valid @RequestBody TelegramAuthRequest request) {
        return authService.telegramAuth(request);
    }

    @GetMapping("/me")
    public UserDto me() {
        return authService.getCurrentUser();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        authService.logout();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        String newAccessToken = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(newAccessToken);
    }
}
