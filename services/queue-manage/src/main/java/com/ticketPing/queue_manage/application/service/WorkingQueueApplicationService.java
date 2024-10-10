package com.ticketPing.queue_manage.application.service;

import com.ticketPing.queue_manage.application.dto.GeneralTokenResponse;
import java.util.UUID;

public interface WorkingQueueApplicationService {
    GeneralTokenResponse getWorkingQueueToken(UUID userId, String performanceName);
    void processQueueTransfer(String tokenValue);
}
