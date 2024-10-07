package com.ticketPing.user.application.service;

import com.ticketPing.user.domain.entity.User;
import com.ticketPing.user.domain.repository.UserRepository;
import com.ticketPing.user.presentation.status.UserErrorCase;
import exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ticketPing.user.application.dto.request.CreateUserRequest;
import response.UserResponse;

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

        return UserResponse.of(savedUser.getId(), savedUser.getEmail(), savedUser.getNickname(),
                savedUser.getBirthday(), savedUser.getGender().getValue());
    }

    @Transactional
    public void validateDuplicateEmail(String email) {
        userRepository.findByEmail(email)
                .ifPresent(u -> {throw new ApplicationException(UserErrorCase.DUPLICATE_EMAIL);});
    }
}
