package com.example.auth.service;

import com.example.auth.client.UserClient;
import com.example.common.dto.UserDto;
import com.example.common.telegram.TelegramAuthRequest;
import com.example.common.telegram.TelegramHashVerifier;
import com.example.common.utils.JwtUtil;
import com.example.redis.RedisTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final TelegramHashVerifier verifier;
    private final UserClient userClient;
    private final JwtUtil jwtUtil;
    private final RedisTokenRepository tokenRepository;
    private final boolean checkHash;

    public AuthService(
            @Value("${telegram.bot-token}") String botToken,
            @Value("${telegram.check-hash:true}") boolean checkHash,
            UserClient userClient,
            RedisTokenRepository tokenRepository
    ) {
        this.verifier = new TelegramHashVerifier(botToken);
        this.userClient = userClient;
        this.tokenRepository = tokenRepository;
        this.jwtUtil = new JwtUtil("supersecretkey-that-is-over-32-bytes-long!!", 3600_000); // 1h
        this.checkHash = checkHash;
    }

    public String telegramAuth(TelegramAuthRequest request) {
        // 🔍 LOG DEBUG: вхідні дані
        System.out.println("➡️ Telegram auth request received:");
        System.out.println("ID: " + request.getTelegramUserId());
        System.out.println("Username: " + request.getUsername());
        System.out.println("First Name: " + request.getFirstName());
        System.out.println("Hash: " + request.getHash());

        // 🧯 Null-check
        if (request.getTelegramUserId() == null || request.getUsername() == null) {
            throw new RuntimeException("❌ Telegram ID or Username is null!");
        }

        // ✅ Перевірка хеша (тільки якщо профіль дозволяє)
        if (checkHash && !verifier.isHashValid(request)) {
            throw new RuntimeException("❌ Invalid Telegram hash");
        }

        // 🔍 Check if user exists by Telegram ID
        if (!userClient.exists(request.getTelegramUserId())) {
            UserDto dto = new UserDto(
                    null,
                    request.getTelegramUserId(),
                    request.getUsername(),
                    request.getFirstName(),
                    request.getLastName(),
                    request.getPhotoUrl(),
                    "USER"
            );

            // LOG
            System.out.println("🧾 Creating new user in user-service...");
            userClient.create(dto);
        }

        // 🛡️ Генерація токенів
        String accessToken = jwtUtil.generateToken(request.getTelegramUserId());
        String refreshToken = jwtUtil.generateRefreshToken(request.getTelegramUserId());

        tokenRepository.saveAccessToken(request.getTelegramUserId(), accessToken);
        tokenRepository.saveRefreshToken(request.getTelegramUserId(), refreshToken);

        System.out.println("✅ User authenticated. AccessToken: " + accessToken);

        return accessToken;
    }
}
