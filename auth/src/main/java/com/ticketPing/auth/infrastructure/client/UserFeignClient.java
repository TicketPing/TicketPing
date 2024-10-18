package com.ticketPing.auth.infrastructure.client;

import com.ticketPing.auth.application.client.UserClient;
import common.response.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import request.LoginRequest;
import response.UserResponse;

import java.util.UUID;

@FeignClient(name = "user")
public interface UserFeignClient extends UserClient {
    @GetMapping("/api/v1/users/login")
    CommonResponse<UserResponse> getUserByEmailAndPassword(@RequestBody LoginRequest loginRequest);

    @GetMapping("/api/v1/users/{userId}")
    CommonResponse<UserResponse> getUser(@RequestParam("userId") UUID userId);
}
