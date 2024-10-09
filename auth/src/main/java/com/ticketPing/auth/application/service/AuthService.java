package com.ticketPing.auth.application.service;

import com.ticketPing.auth.presentation.request.LoginRequest;
import com.ticketPing.auth.application.dto.LoginResponse;
import com.ticketPing.auth.domain.entity.User;
import com.ticketPing.auth.domain.repository.UserRepository;
import com.ticketPing.auth.presentation.cases.AuthErrorCase;
import com.ticketPing.auth.security.JwtUtil;
import com.ticketPing.auth.security.Role;
import common.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        User user = findUserByEmail(loginRequest.email());

        if(!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new ApplicationException(AuthErrorCase.PASSWORD_NOT_EQUAL);
        }

        String jwtToken =  jwtUtil.createToken(user.getEmail(), Role.USER);
        return new LoginResponse(jwtToken);
    }

    @Transactional
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ApplicationException(AuthErrorCase.USER_NOT_FOUND));
    }

    @Transactional
    public void verifyUser(UUID userId) {
        User user = findUserById(userId);
        // TODO: 레디스에 회원 정보 넣기
    }

    @Transactional
    public User findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(AuthErrorCase.USER_NOT_FOUND));
    }
}
