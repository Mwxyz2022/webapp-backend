package com.example.chat.repository;

import com.example.chat.model.ChatMessageEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessageEntity, String> {
    List<ChatMessageEntity> findByChatId(String chatId);
}
