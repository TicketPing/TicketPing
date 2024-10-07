package com.ticketPing.queue_manage.presentaion.controller;

import static com.ticketPing.queue_manage.domain.cases.QueueSuccessCase.ENTER_WAITING_QUEUE_SUCCESS;
import static com.ticketPing.queue_manage.domain.cases.QueueSuccessCase.GET_WAITING_QUEUE_INFO_SUCCESS;
import static response.CommonResponse.success;

import com.ticketPing.queue_manage.application.dto.WaitingQueueInfoResponse;
import com.ticketPing.queue_manage.application.service.WaitingQueueApplicationService;
import com.ticketPing.queue_manage.application.dto.EnterWaitingQueueResponse;
import com.ticketPing.queue_manage.presentaion.request.EnterWaitingQueueRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import response.CommonResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/waiting-queue")
public class WaitingQueueController {

    private final WaitingQueueApplicationService waitingQueueService;

    @Operation(summary = "대기열 진입")
    @PostMapping
    public CommonResponse<EnterWaitingQueueResponse> enterWaitingQueue(@Valid @RequestBody EnterWaitingQueueRequest request) {
        return success(ENTER_WAITING_QUEUE_SUCCESS,waitingQueueService.enterWaitingQueue(request));
    }

    @Operation(summary = "대기열 상태 확인")
    @GetMapping("/{userId}")
    public CommonResponse<WaitingQueueInfoResponse> getWaitingQueueInfo(@Valid @PathVariable("userId") UUID userId) {
        return success(GET_WAITING_QUEUE_INFO_SUCCESS, waitingQueueService.getWaitingQueueInfo(userId));
    }

}
