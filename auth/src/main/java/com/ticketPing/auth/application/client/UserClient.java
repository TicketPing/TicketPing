package com.ticketPing.auth.application.client;

import common.response.CommonResponse;
import request.LoginRequest;
import response.UserResponse;

public interface UserClient {
    CommonResponse<UserResponse> getUserByEmailAndPassword(LoginRequest request);
}
