package com.example.user.service;

import com.example.common.dto.UserDto;
import com.example.user.mapper.UserMapper;
import com.example.user.entity.UserEntity;
import com.example.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public boolean existsByTelegramUserId(String telegramUserId) {
        boolean exists = userRepository.existsByTelegramUserId(telegramUserId);
        log.debug("Перевірка на існування користувача {}: {}", telegramUserId, exists);
        return exists;
    }

    @Override
    public void createUser(UserDto dto) {
        UserEntity user = userMapper.toEntity(dto);
        userRepository.save(user);
        log.info("Створено нового користувача з Telegram ID: {}", dto.getTelegramUserId());
    }

    @Override
    public UserDto getByTelegramUserId(String telegramUserId) {
        Optional<UserEntity> optional = userRepository.findByTelegramUserId(telegramUserId);

        if (optional.isEmpty()) {
            log.warn("Користувача з Telegram ID {} не знайдено", telegramUserId);
            throw new RuntimeException("User not found with Telegram ID: " + telegramUserId);
        }

        log.debug("Отримано дані користувача з Telegram ID: {}", telegramUserId);
        return userMapper.toDto(optional.get());
    }
}
