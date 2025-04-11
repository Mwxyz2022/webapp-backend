package com.example.auth.service;

import com.example.auth.client.UserClient;
import com.example.common.dto.UserDto;
import com.example.common.telegram.TelegramAuthRequest;
import com.example.common.telegram.TelegramHashVerifier;
import com.example.common.utils.JwtUtil;
import com.example.redis.RedisTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
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
            @Value("${jwt.secret}") String jwtSecret,
            UserClient userClient,
            RedisTokenRepository tokenRepository
    ) {
        this.verifier = new TelegramHashVerifier(botToken);
        this.userClient = userClient;
        this.tokenRepository = tokenRepository;
        this.jwtUtil = new JwtUtil(jwtSecret, 3600_000); // 1 година
        this.checkHash = checkHash;
    }

    public String telegramAuth(TelegramAuthRequest request) {

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

            userClient.create(dto);
        }

        // 🛡️ Генерація токенів
        String accessToken = jwtUtil.generateToken(request.getTelegramUserId());
        String refreshToken = jwtUtil.generateRefreshToken(request.getTelegramUserId());

        tokenRepository.saveAccessToken(request.getTelegramUserId(), accessToken);
        tokenRepository.saveRefreshToken(request.getTelegramUserId(), refreshToken);

        return accessToken;
    }

    public UserDto getCurrentUser() {
        // ⚠️ Витягуємо Telegram ID із контексту безпеки
        String telegramUserId = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        return userClient.getByTelegramUserId(telegramUserId);
    }
}
