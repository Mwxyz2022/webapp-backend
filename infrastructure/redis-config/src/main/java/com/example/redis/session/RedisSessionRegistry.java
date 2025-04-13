package com.example.redis.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;
import java.util.Optional;


public class RedisSessionRegistry {

    private static final Logger log = LoggerFactory.getLogger(RedisSessionRegistry.class);

    private static final String SESSION_KEY_PREFIX = "chat:session:";
    private static final Duration TTL = Duration.ofHours(6); // сесія зникає автоматично через 6 годин

    private final StringRedisTemplate redisTemplate;

    public RedisSessionRegistry(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveSession(String sessionId, String managerId) {
        String key = buildKey(sessionId);
        redisTemplate.opsForValue().set(key, managerId, TTL);
        log.debug("🔐 Збережено сесію: [{}] -> [{}]", sessionId, managerId);
    }

    public Optional<String> getManagerIdBySession(String sessionId) {
        String key = buildKey(sessionId);
        String managerId = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(managerId);
    }

    public void removeSession(String sessionId) {
        String key = buildKey(sessionId);
        redisTemplate.delete(key);
        log.debug("❌ Видалено сесію: [{}]", sessionId);
    }

    private String buildKey(String sessionId) {
        return SESSION_KEY_PREFIX + sessionId;
    }
}
