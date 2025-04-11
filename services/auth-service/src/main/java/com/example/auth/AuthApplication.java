package com.example.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

// ✅ Явно вказуємо всі потрібні базові пакети
@SpringBootApplication(scanBasePackages = {
        "com.example.auth",
        "com.example.common",
        "com.example.redis"  // 👈 обов’язково для RedisTokenRepositoryImpl
})
@EnableFeignClients(basePackages = "com.example.auth.client")
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}