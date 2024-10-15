package com.ticketPing.order.client;

import com.ticketPing.order.application.dtos.temp.UserResponse;
import common.response.CommonResponse;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user")
public interface UserClient {
    @GetMapping("/api/v1/users/{userId}")
    ResponseEntity<CommonResponse<UserResponse>> getUser(@PathVariable("userId") UUID userId);
}
