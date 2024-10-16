package com.ticketPing.queue_manage.application.service;

import com.ticketPing.queue_manage.application.dto.GeneralTokenResponse;
import com.ticketPing.queue_manage.domain.model.enums.DeleteWorkingTokenCase;

public interface WorkingQueueApplicationService {
    GeneralTokenResponse getWorkingQueueToken(String userId, String performanceId);
    void processQueueTransfer(DeleteWorkingTokenCase deleteCase, String tokenValue);
}
