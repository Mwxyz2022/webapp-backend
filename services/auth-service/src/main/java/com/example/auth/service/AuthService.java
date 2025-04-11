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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

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
            log.warn("TelegramAuthRequest is missing ID or username");
            throw new IllegalArgumentException("Telegram ID or Username is null!");
        }

        if (checkHash && !verifier.isHashValid(request)) {
            log.warn("Invalid Telegram hash for user ID: {}", request.getTelegramUserId());
            throw new InvalidTelegramHashException();
        }

        if (!userClient.exists(request.getTelegramUserId())) {
            log.info("New user detected. Creating user with Telegram ID: {}", request.getTelegramUserId());
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

        log.info("Access + Refresh токени створені для користувача {}", request.getTelegramUserId());
        return accessToken;
    }

    public UserDto getCurrentUser() {
        String telegramUserId = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        log.debug("Get current user: {}", telegramUserId);
        return userClient.getByTelegramUserId(telegramUserId);
    }

    public void logout() {
        String telegramUserId = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        tokenRepository.deleteAccessToken(telegramUserId);
        tokenRepository.deleteRefreshToken(telegramUserId);
        log.info("Logout: токени видалено для користувача {}", telegramUserId);
    }

    public String refresh(String refreshToken) {
        try {
            Claims claims = jwtUtil.parseToken(refreshToken);
            String telegramUserId = claims.getSubject();

            String storedRefresh = tokenRepository.getAccessToken(telegramUserId);
            if (storedRefresh == null || !storedRefresh.equals(refreshToken)) {
                log.warn("Refresh token не знайдено в Redis або не співпадає для {}", telegramUserId);
                throw new TokenNotFoundException();
            }

            String newAccessToken = jwtUtil.generateToken(telegramUserId);
            tokenRepository.saveAccessToken(telegramUserId, newAccessToken);
            log.info("Новий access токен видано для користувача {}", telegramUserId);
            return newAccessToken;

        } catch (TokenNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Помилка при оновленні токена", e);
            throw new RuntimeException("Failed to refresh token", e);
        }
    }
}
