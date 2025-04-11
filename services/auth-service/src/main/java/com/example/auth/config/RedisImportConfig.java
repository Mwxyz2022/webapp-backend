package com.example.auth.config;

import com.example.redis.config.RedisConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RedisConfig.class)
public class RedisImportConfig {
}