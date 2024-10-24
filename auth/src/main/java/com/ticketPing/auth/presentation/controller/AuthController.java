package com.ticketPing.auth.presentation.controller;

import com.ticketPing.auth.application.dto.LoginResponse;
import com.ticketPing.auth.application.dto.UserCacheDto;
import com.ticketPing.auth.application.service.AuthService;
import com.ticketPing.auth.presentation.cases.AuthSuccessCase;
import com.ticketPing.auth.presentation.request.AuthLoginRequest;
import common.response.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<LoginResponse>> login(@RequestBody @Valid final AuthLoginRequest authLoginRequest) {
        LoginResponse loginResponse = authService.login(authLoginRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(AuthSuccessCase.SUCCESS_LOGIN, loginResponse));
    }

    @GetMapping("/validate")
    public ResponseEntity<UserCacheDto> validateToken(String token) {
        UserCacheDto response = authService.validateToken(token);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

}
