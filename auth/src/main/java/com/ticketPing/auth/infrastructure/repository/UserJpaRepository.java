package com.ticketPing.auth.infrastructure.repository;

import com.ticketPing.auth.domain.entity.User;
import com.ticketPing.auth.domain.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<User, UUID>, UserRepository { }
