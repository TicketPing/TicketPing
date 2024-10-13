package com.ticketPing.user.presentation.controller;

import com.ticketPing.user.application.dto.UserResponse;
import com.ticketPing.user.application.service.UserService;
import com.ticketPing.user.presentation.cases.UserSuccessCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ticketPing.user.presentation.request.CreateUserRequest;
import common.response.CommonResponse;
import request.LoginRequest;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<UserResponse>> createUser(@RequestBody @Valid CreateUserRequest request) {
        UserResponse userResponse = userService.createUser(request);
        return ResponseEntity
                .status(201)
                .body(CommonResponse.success(UserSuccessCase.SUCCESS_CREATE_USER, userResponse));
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<UserResponse>> getUserByEmailAndPassword(@RequestBody LoginRequest request) {
        UserResponse userResponse = userService.getUserByEmailAndPassword(request);
        return ResponseEntity
                .status(200)
                .body(CommonResponse.success(UserSuccessCase.SUCCESS_GET_USER, userResponse));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CommonResponse<UserResponse>> getUser(@RequestParam("userId") UUID userId) {
        UserResponse userResponse = userService.getUser(userId);
        return ResponseEntity
                .status(200)
                .body(CommonResponse.success(UserSuccessCase.SUCCESS_GET_USER, userResponse));
    }
}
