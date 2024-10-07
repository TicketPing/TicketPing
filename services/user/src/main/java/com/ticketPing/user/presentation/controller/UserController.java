package com.ticketPing.user.presentation.controller;

import com.ticketPing.user.application.service.UserService;
import com.ticketPing.user.presentation.status.UserSuccessCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ticketPing.user.application.dto.request.CreateUserRequest;
import response.CommonResponse;
import response.UserResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<CommonResponse<UserResponse>> createUser(@RequestBody @Valid CreateUserRequest request) {
        UserResponse userResponse = userService.createUser(request);
        return ResponseEntity
                .status(201)
                .body(CommonResponse.success(UserSuccessCase.SUCCESS_CREATE_USER, userResponse));
    }
}
