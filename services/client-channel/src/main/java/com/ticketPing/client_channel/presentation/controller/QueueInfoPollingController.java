package com.ticketPing.client_channel.presentation.controller;

import static com.ticketPing.client_channel.presentation.cases.ClientSuccessCase.GET_WAITING_QUEUE_INFO_SUCCESS;
import static common.response.CommonResponse.success;

import com.ticketPing.client_channel.application.dto.WaitingQueueInfoResponse;
import com.ticketPing.client_channel.application.service.QueueInfoPollingService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import common.response.CommonResponse;

@RestController
@RequiredArgsConstructor
public class QueueInfoPollingController {

    private final QueueInfoPollingService queueInfoPollingService;

    @Operation(summary = "대기열 상태 확인 Polling")
    @GetMapping("/api/waiting-queue/{userId}")
    public CommonResponse<WaitingQueueInfoResponse> getWaitingQueueInfo(@Valid @PathVariable("userId") UUID userId) {
        return success(GET_WAITING_QUEUE_INFO_SUCCESS, queueInfoPollingService.getWaitingQueueInfo(userId));
    }

}
