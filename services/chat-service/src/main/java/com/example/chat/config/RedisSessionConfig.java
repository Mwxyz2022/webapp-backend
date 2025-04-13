package com.example.chat.config;

import com.example.redis.session.RedisSessionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisSessionConfig {

    @Bean
    public RedisSessionRegistry redisSessionRegistry(StringRedisTemplate redisTemplate) {
        return new RedisSessionRegistry(redisTemplate);
    }
}
