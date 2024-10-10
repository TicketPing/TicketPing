package com.ticketPing.queue_manage.application.service;

import com.ticketPing.queue_manage.application.dto.GeneralTokenResponse;
import java.util.UUID;

public interface WaitingQueueApplicationService {
    GeneralTokenResponse enterWaitingQueue(UUID userId, String performanceName);
    GeneralTokenResponse getQueueToken(UUID userId, String performanceName);
}
