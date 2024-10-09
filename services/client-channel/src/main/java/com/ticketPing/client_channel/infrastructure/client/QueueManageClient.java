package com.ticketPing.client_channel.infrastructure.client;

import com.ticketPing.client_channel.application.dto.GeneralTokenResponse;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import common.response.CommonResponse;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "queue-manage")
public interface QueueManageClient {
    @GetMapping("/api/waiting-queue/{userId}")
    CommonResponse<GeneralTokenResponse> getWaitingQueueInfo(@PathVariable("userId") UUID userId, @RequestParam("performanceName") String performanceName);
}
