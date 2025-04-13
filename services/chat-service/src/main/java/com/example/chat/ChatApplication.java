package com.example.chat;

import com.example.common.config.EnableCommonModule;
import com.example.kafka.config.EnableKafkaModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableCommonModule
@EnableKafkaModule
public class ChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }
}
