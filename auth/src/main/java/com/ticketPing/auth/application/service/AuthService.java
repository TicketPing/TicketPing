package com.ticketPing.auth.application.service;

import com.ticketPing.auth.application.client.UserClient;
import com.ticketPing.auth.application.dto.LoginResponse;
import com.ticketPing.auth.application.dto.ValidateTokenResponse;
import com.ticketPing.auth.infrastructure.security.JwtUtil;
import com.ticketPing.auth.infrastructure.security.Role;
import com.ticketPing.auth.presentation.cases.AuthErrorCase;
import com.ticketPing.auth.presentation.request.AuthLoginRequest;
import common.exception.ApplicationException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import request.LoginRequest;
import response.UserResponse;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtil jwtUtil;
    private final UserClient userClient;

    public LoginResponse login(AuthLoginRequest authLoginRequest) {
        LoginRequest loginRequest = new LoginRequest(authLoginRequest.email(), authLoginRequest.password());
        UserResponse userResponse = userClient.getUserByEmailAndPassword(loginRequest).getData();

        String jwtToken = jwtUtil.createToken(userResponse.userId().toString(), Role.USER);

        // TODO: 레디스에 회원 정보 캐싱

        return new LoginResponse(jwtToken);
    }

    public ValidateTokenResponse validateToken(String jwtToken) {
        jwtUtil.validateToken(jwtToken);
        Claims claims = jwtUtil.getClaimsFromToken(jwtToken);
        UUID userId = UUID.fromString(claims.getSubject());
        String role = claims.get("auth", String.class);

        validateUser(userId, role);

        // TODO: 레디스에 회원 정보 캐싱

        return new ValidateTokenResponse(userId, role);
    }

    public void validateUser(UUID userId, String role) {
        Role enumRole = Role.valueOf(role);

        if(enumRole.equals(Role.USER)) {
            userClient.getUser(userId);
        } else {
            throw new ApplicationException(AuthErrorCase.INVALID_ROLE);
        }
    }
}
