package com.ticketPing.auth.application.service;

import com.ticketPing.auth.application.client.UserClient;
import com.ticketPing.auth.presentation.request.AuthLoginRequest;
import com.ticketPing.auth.application.dto.LoginResponse;
import com.ticketPing.auth.domain.entity.User;
import com.ticketPing.auth.domain.repository.UserRepository;
import com.ticketPing.auth.presentation.cases.AuthErrorCase;
import com.ticketPing.auth.security.JwtUtil;
import com.ticketPing.auth.security.Role;
import common.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import request.LoginRequest;
import response.UserResponse;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final UserClient userClient;

    @Transactional
    public LoginResponse login(AuthLoginRequest authLoginRequest) {
        LoginRequest loginRequest = new LoginRequest(authLoginRequest.email(), authLoginRequest.password());
        UserResponse userResponse = userClient.getUserByEmailAndPassword(loginRequest).getData();

        String jwtToken = jwtUtil.createToken(authLoginRequest.email(), Role.USER);

        // TODO: 레디스에 회원 정보 캐싱

        return new LoginResponse(jwtToken);
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
