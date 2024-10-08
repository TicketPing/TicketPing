package com.ticketPing.auth.presentation.controller;

import com.ticketPing.auth.application.dto.request.LoginRequest;
import com.ticketPing.auth.application.dto.response.LoginResponse;
import com.ticketPing.auth.application.service.AuthService;
import com.ticketPing.auth.presentation.status.AuthSuccessCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import common.response.CommonResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<LoginResponse>> login(@RequestBody final LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(AuthSuccessCase.SUCCESS_LOGIN, loginResponse));
    }

}
