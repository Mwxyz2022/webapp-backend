package com.example.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
public class RedisTokenRepositoryImpl implements RedisTokenRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisTokenRepositoryImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void saveAccessToken(String telegramUserId, String token) {
        redisTemplate.opsForValue().set("access:" + telegramUserId, token, Duration.ofHours(1));
    }

    @Override
    public void saveRefreshToken(String telegramUserId, String token) {
        redisTemplate.opsForValue().set("refresh:" + telegramUserId, token, Duration.ofDays(7));
    }

    @Override
    public String getAccessToken(String telegramUserId) {
        return redisTemplate.opsForValue().get("access:" + telegramUserId);
    }

    @Override
    public void deleteAccessToken(String telegramUserId) {
        redisTemplate.delete("access:" + telegramUserId);
    }

    @Override
    public void deleteRefreshToken(String telegramUserId) {
        redisTemplate.delete("refresh:" + telegramUserId);
    }
}
