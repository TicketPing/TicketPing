package com.ticketPing.queue_manage.application.service;

import com.ticketPing.queue_manage.application.dto.GeneralTokenResponse;

public interface WorkingQueueApplicationService {
    GeneralTokenResponse getWorkingQueueToken(String userId, String performanceId);
    void processQueueTransfer(String tokenValue);
}
