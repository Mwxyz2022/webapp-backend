package com.example.user.service;

import com.example.common.dto.UserDto;
import com.example.user.mapper.UserMapper;
import com.example.user.entity.UserEntity;
import com.example.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public boolean existsByTelegramUserId(String telegramUserId) {
        return userRepository.existsByTelegramUserId(telegramUserId);
    }

    @Override
    public void createUser(UserDto dto) {
        UserEntity user = userMapper.toEntity(dto);
        userRepository.save(user);
    }

    @Override
    public UserDto getByTelegramUserId(String telegramUserId) {
        Optional<UserEntity> optional = userRepository.findByTelegramUserId(telegramUserId);

        UserEntity entity = optional.orElseThrow(() ->
                new RuntimeException("User not found with Telegram ID: " + telegramUserId));

        return userMapper.toDto(entity);
    }
}
