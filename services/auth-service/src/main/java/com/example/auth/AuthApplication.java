package com.example.auth;

import com.example.auth.client.config.EnableUserClient;
import com.example.common.config.EnableCommonModule;
import com.example.redis.config.EnableRedisModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableCommonModule
@EnableRedisModule
@EnableUserClient
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}