package com.ticketPing.queue_manage.presentaion.controller;

import static com.ticketPing.queue_manage.presentaion.cases.QueueSuccessCase.GET_WORKING_QUEUE_TOKEN_SUCCESS;
import static common.response.CommonResponse.success;

import com.ticketPing.queue_manage.application.dto.GeneralTokenResponse;
import com.ticketPing.queue_manage.application.service.WorkingQueueApplicationService;
import common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/working-queue")
public class WorkingQueueController {

    private final WorkingQueueApplicationService workingQueueService;

    @Operation(summary = "작업열 토큰 조회")
    @GetMapping("/{userId}")
    public CommonResponse<GeneralTokenResponse> getWorkingQueueToken(@Valid @PathVariable("userId") UUID userId,
                                                                     @Valid @RequestParam("performanceName") String performanceName) {
        return success(GET_WORKING_QUEUE_TOKEN_SUCCESS, workingQueueService.getWorkingQueueToken(userId, performanceName));
    }

}
