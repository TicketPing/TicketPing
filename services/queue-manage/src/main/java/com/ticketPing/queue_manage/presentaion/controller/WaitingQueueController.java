package com.ticketPing.queue_manage.presentaion.controller;

import com.ticketPing.queue_manage.application.dto.GeneralQueueTokenResponse;
import com.ticketPing.queue_manage.application.service.WaitingQueueService;
import common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/waiting-queue")
public class WaitingQueueController {

    private final WaitingQueueService waitingQueueService;

    @Operation(summary = "대기열 진입")
    @PostMapping("/{userId}")
    public Mono<ResponseEntity<CommonResponse<GeneralQueueTokenResponse>>> enterWaitingQueue(@Valid @PathVariable("userId") String userId,
                                                                                             @Valid @RequestParam("performanceId") String performanceId) {
        return waitingQueueService.enterWaitingQueue(userId, performanceId)
                .map(CommonResponse::success)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "대기열 상태 조회")
    @GetMapping("/{userId}")
    public Mono<ResponseEntity<CommonResponse<GeneralQueueTokenResponse>>> getQueueInfo(@Valid @PathVariable("userId") String userId,
                                                                                        @Valid @RequestParam("performanceId") String performanceId) {
        return waitingQueueService.getQueueInfo(userId, performanceId)
                .map(CommonResponse::success)
                .map(ResponseEntity::ok);
    }

}
