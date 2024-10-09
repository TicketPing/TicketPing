package com.ticketPing.queue_manage.application.service;

import com.ticketPing.queue_manage.application.dto.EnterWaitingQueueResponse;
import com.ticketPing.queue_manage.application.dto.GeneralTokenResponse;
import java.util.UUID;

public interface WaitingQueueApplicationService {
    EnterWaitingQueueResponse enterWaitingQueue(UUID userId);
    GeneralTokenResponse getQueueToken(UUID userId);
}
