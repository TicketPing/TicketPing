package com.ticketPing.user.infrastructure.repository;

import com.ticketPing.user.domain.entity.User;
import com.ticketPing.user.domain.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<User, UUID>, UserRepository {
}
