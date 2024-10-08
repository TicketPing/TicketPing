package com.ticketPing.client_channel.infrastructure.client;

import com.ticketPing.client_channel.application.dto.WaitingQueueInfoResponse;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import common.response.CommonResponse;

@FeignClient(name = "queue-manage")
public interface QueueManageClient {
    @GetMapping("/api/waiting-queue/{userId}")
    CommonResponse<WaitingQueueInfoResponse> getWaitingQueueInfo(@PathVariable("userId") UUID userId);
}
