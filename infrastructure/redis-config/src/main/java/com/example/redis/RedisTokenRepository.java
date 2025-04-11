package com.example.redis;

public interface RedisTokenRepository {

    void saveAccessToken(String telegramUserId, String token);

    void saveRefreshToken(String telegramUserId, String token);

    String getAccessToken(String telegramUserId);

    void deleteAccessToken(String telegramUserId);

    void deleteRefreshToken(String telegramUserId);
}
