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
    public void saveAccessToken(String username, String token) {
        redisTemplate.opsForValue().set("access:" + username, token, Duration.ofHours(1));
    }

    @Override
    public void saveRefreshToken(String username, String token) {
        redisTemplate.opsForValue().set("refresh:" + username, token, Duration.ofDays(7));
    }
}
