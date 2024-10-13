package com.ticketPing.auth.infrastructure.client;

import com.ticketPing.auth.application.client.UserClient;
import common.response.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import request.LoginRequest;
import response.UserResponse;

@FeignClient(name = "user")
public interface UserClientImpl extends UserClient {
    @PostMapping("/api/v1/users/login")
    CommonResponse<UserResponse> getUserByEmailAndPassword(@RequestBody LoginRequest request);
}

