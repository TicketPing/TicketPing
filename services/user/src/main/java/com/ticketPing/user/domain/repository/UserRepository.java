package com.ticketPing.user.domain.repository;

import com.ticketPing.user.domain.entity.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);

    Optional<User> findByEmail(String email);
}
