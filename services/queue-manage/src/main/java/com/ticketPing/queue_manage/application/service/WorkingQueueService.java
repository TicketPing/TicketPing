package com.ticketPing.queue_manage.application.service;

import com.ticketPing.queue_manage.application.dto.GeneralQueueTokenResponse;
import com.ticketPing.queue_manage.domain.model.enums.DeleteWorkingTokenCase;

public interface WorkingQueueService {
    GeneralQueueTokenResponse getWorkingQueueToken(String userId, String performanceId);
    void processQueueTransfer(DeleteWorkingTokenCase deleteCase, String tokenValue);
}
