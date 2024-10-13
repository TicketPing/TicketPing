package com.ticketPing.queue_manage.application.service;

import com.ticketPing.queue_manage.application.dto.GeneralTokenResponse;

public interface WaitingQueueApplicationService {
    GeneralTokenResponse enterWaitingQueue(String userId, String performanceId);
    GeneralTokenResponse getQueueToken(String userId, String performanceId);
}
