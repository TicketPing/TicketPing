package com.ticketPing.queue_manage.presentaion.controller;

import static com.ticketPing.queue_manage.presentaion.cases.QueueSuccessCase.ENTER_WAITING_QUEUE_SUCCESS;
import static com.ticketPing.queue_manage.presentaion.cases.QueueSuccessCase.GET_QUEUE_TOKEN_SUCCESS;
import static common.response.CommonResponse.success;

import com.ticketPing.queue_manage.application.dto.GeneralTokenResponse;
import com.ticketPing.queue_manage.application.service.WaitingQueueApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import common.response.CommonResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/waiting-queue")
public class WaitingQueueController {

    private final WaitingQueueApplicationService waitingQueueService;

    @Operation(summary = "대기열 진입")
    @PostMapping("/{userId}")
    public CommonResponse<GeneralTokenResponse> enterWaitingQueue(@Valid @PathVariable("userId") String userId,
                                                                  @Valid @RequestParam("performanceId") String performanceId) {
        return success(ENTER_WAITING_QUEUE_SUCCESS,waitingQueueService.enterWaitingQueue(userId, performanceId));
    }

    @Operation(summary = "대기열 상태 조회")
    @GetMapping("/{userId}")
    public CommonResponse<GeneralTokenResponse> getQueueToken(@Valid @PathVariable("userId") String userId,
                                                              @Valid @RequestParam("performanceId") String performanceId) {
        return success(GET_QUEUE_TOKEN_SUCCESS, waitingQueueService.getQueueToken(userId, performanceId));
    }

}
