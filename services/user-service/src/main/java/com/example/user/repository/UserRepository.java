package com.example.user.repository;

import com.example.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByTelegramUserId(String telegramUserId);
    Optional<UserEntity> findByTelegramUserId(String telegramUserId);
}
