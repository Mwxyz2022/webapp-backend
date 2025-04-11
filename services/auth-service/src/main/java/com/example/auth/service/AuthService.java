package com.example.auth.service;

import com.example.auth.client.UserClient;
import com.example.common.dto.UserDto;
import com.example.common.exceptions.InvalidTelegramHashException;
import com.example.common.exceptions.TokenNotFoundException;
import com.example.common.telegram.TelegramAuthRequest;
import com.example.common.telegram.TelegramHashVerifier;
import com.example.common.utils.JwtUtil;
import com.example.redis.RedisTokenRepository;
import io.jsonwebtoken.Claims;
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
        if (request.getTelegramUserId() == null || request.getUsername() == null) {
            throw new IllegalArgumentException("Telegram ID or Username is null!");
        }

        if (checkHash && !verifier.isHashValid(request)) {
            throw new InvalidTelegramHashException();
        }

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

        String accessToken = jwtUtil.generateToken(request.getTelegramUserId());
        String refreshToken = jwtUtil.generateRefreshToken(request.getTelegramUserId());

        tokenRepository.saveAccessToken(request.getTelegramUserId(), accessToken);
        tokenRepository.saveRefreshToken(request.getTelegramUserId(), refreshToken);

        return accessToken;
    }

    public UserDto getCurrentUser() {
        String telegramUserId = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        return userClient.getByTelegramUserId(telegramUserId);
    }

    public void logout() {
        String telegramUserId = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        tokenRepository.deleteAccessToken(telegramUserId);
        tokenRepository.deleteRefreshToken(telegramUserId);
    }

    public String refresh(String refreshToken) {
        try {
            Claims claims = jwtUtil.parseToken(refreshToken);
            String telegramUserId = claims.getSubject();

            String storedRefresh = tokenRepository.getAccessToken(telegramUserId);
            if (storedRefresh == null || !storedRefresh.equals(refreshToken)) {
                throw new TokenNotFoundException();
            }

            String newAccessToken = jwtUtil.generateToken(telegramUserId);
            tokenRepository.saveAccessToken(telegramUserId, newAccessToken);
            return newAccessToken;

        } catch (TokenNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to refresh token", e);
        }
    }
}
