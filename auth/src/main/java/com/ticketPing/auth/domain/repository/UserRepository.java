package com.ticketPing.auth.domain.repository;

import com.ticketPing.auth.domain.entity.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);
}
