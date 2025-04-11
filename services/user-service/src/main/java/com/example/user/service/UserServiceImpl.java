package com.example.user.service;

import com.example.common.dto.UserDto;
import com.example.user.entity.UserEntity;
import com.example.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean existsByTelegramUserId(String telegramUserId) {
        return userRepository.existsByTelegramUserId(telegramUserId);
    }

    @Override
    public void createUser(UserDto dto) {
        UserEntity user = new UserEntity();
        user.setTelegramUserId(dto.getTelegramUserId());
        user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhotoUrl(dto.getPhotoUrl());
        user.setRole(dto.getRole());
        userRepository.save(user);
    }

    @Override
    public UserDto getByTelegramUserId(String telegramUserId) {
        Optional<UserEntity> optional = userRepository.findByTelegramUserId(telegramUserId);

        UserEntity entity = optional.orElseThrow(() ->
                new RuntimeException("User not found with Telegram ID: " + telegramUserId));

        return new UserDto(
                entity.getId(),
                entity.getTelegramUserId(),
                entity.getUsername(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getPhotoUrl(),
                entity.getRole()
        );
    }
}
