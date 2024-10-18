package com.ticketPing.auth.application.client;

import common.response.CommonResponse;
import request.LoginRequest;
import response.UserResponse;

import java.util.UUID;

public interface UserClient {
    CommonResponse<UserResponse> getUserByEmailAndPassword(LoginRequest loginRequest);
    CommonResponse<UserResponse> getUser(UUID userId);
}
