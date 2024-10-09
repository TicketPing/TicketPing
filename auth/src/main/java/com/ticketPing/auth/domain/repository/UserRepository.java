package com.ticketPing.auth.domain.repository;

import com.ticketPing.auth.domain.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findByEmail(String email);

    Optional<User> findById(UUID userId);
}
