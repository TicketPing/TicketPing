package com.ticketPing.queue_manage.application.service;

import com.ticketPing.queue_manage.application.dto.GeneralQueueTokenResponse;

public interface WaitingQueueService {
    GeneralQueueTokenResponse enterWaitingQueue(String userId, String performanceId);
    GeneralQueueTokenResponse getQueueInfo(String userId, String performanceId);
}
