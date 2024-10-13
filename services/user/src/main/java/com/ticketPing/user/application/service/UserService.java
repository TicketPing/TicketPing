package com.ticketPing.user.application.service;

import com.ticketPing.user.application.dto.UserResponse;
import com.ticketPing.user.domain.entity.User;
import com.ticketPing.user.domain.repository.UserRepository;
import com.ticketPing.user.presentation.cases.UserErrorCase;
import common.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ticketPing.user.presentation.request.CreateUserRequest;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        validateDuplicateEmail(request.email());
        String encodedPassword = passwordEncoder.encode(request.password());

        User user = User.from(request, encodedPassword);
        User savedUser = userRepository.save(user);

        return UserResponse.of(savedUser);
    }

    @Transactional
    public void validateDuplicateEmail(String email) {
        userRepository.findByEmail(email)
                .ifPresent(u -> {throw new ApplicationException(UserErrorCase.DUPLICATE_EMAIL);});
    }

    @Transactional(readOnly = true)
    public UserResponse getUser(UUID userId) {
        User user = findUserById(userId);
        return UserResponse.of(user);
    }

    @Transactional
    public User findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(UserErrorCase.USER_NOT_FOUND));
    }
}
