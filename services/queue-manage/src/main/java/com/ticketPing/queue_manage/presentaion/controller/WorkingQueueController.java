package com.ticketPing.queue_manage.presentaion.controller;

import static com.ticketPing.queue_manage.presentaion.cases.QueueSuccessCase.GET_WORKING_QUEUE_TOKEN;
import static common.response.CommonResponse.success;

import com.ticketPing.queue_manage.application.dto.GeneralQueueTokenResponse;
import com.ticketPing.queue_manage.application.service.WorkingQueueService;
import common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/working-queue")
public class WorkingQueueController {

    private final WorkingQueueService workingQueueService;

    @Operation(summary = "작업열 토큰 조회")
    @GetMapping("/{userId}")
    public CommonResponse<GeneralQueueTokenResponse> getWorkingQueueToken(@Valid @PathVariable("userId") String userId,
                                                                          @Valid @RequestParam("performanceId") String performanceId) {
        return success(GET_WORKING_QUEUE_TOKEN, workingQueueService.getWorkingQueueToken(userId, performanceId));
    }

}
