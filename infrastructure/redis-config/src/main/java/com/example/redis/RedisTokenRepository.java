package com.example.redis;

public interface RedisTokenRepository {
    void saveAccessToken(String username, String token);

    void saveRefreshToken(String username, String token);
}