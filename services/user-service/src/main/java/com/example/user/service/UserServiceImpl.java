package com.example.user.service;

import com.example.common.dto.UserDto;
import com.example.user.entity.UserEntity;
import com.example.user.repository.UserRepository;
import org.springframework.stereotype.Service;

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
}
