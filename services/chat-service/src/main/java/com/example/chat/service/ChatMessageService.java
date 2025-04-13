package com.example.chat.service;

import com.example.chat.model.ChatMessage;
import com.example.chat.model.ChatMessageEntity;
import com.example.chat.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ChatMessageService {

    private final ChatMessageRepository repository;

    public ChatMessageService(ChatMessageRepository repository) {
        this.repository = repository;
    }

    public void save(String chatId, String from, String to, String text) {
        ChatMessageEntity entity = new ChatMessageEntity(
                chatId, from, to, text, Instant.now()
        );
        repository.save(entity);
    }

    public List<ChatMessage> getMessages(String chatId) {
        return repository.findByChatId(chatId)
                .stream()
                .map(entity -> new ChatMessage(
                        entity.getChatId(),
                        entity.getFrom(),
                        entity.getTo(),
                        entity.getText(),
                        entity.getTimestamp()
                ))
                .toList();
    }
}
