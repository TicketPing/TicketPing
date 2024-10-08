package com.ticketPing.auth.application.service;

import com.ticketPing.auth.application.dto.request.LoginRequest;
import com.ticketPing.auth.application.dto.response.LoginResponse;
import com.ticketPing.auth.domain.entity.User;
import com.ticketPing.auth.domain.repository.UserRepository;
import com.ticketPing.auth.presentation.status.AuthErrorCase;
import com.ticketPing.auth.security.JwtUtil;
import com.ticketPing.auth.security.Role;
import exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
