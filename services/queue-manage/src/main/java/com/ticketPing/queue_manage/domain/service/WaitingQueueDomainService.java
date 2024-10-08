package com.ticketPing.queue_manage.domain.service;

import com.ticketPing.queue_manage.domain.model.WaitingQueueToken;
import com.ticketPing.queue_manage.presentaion.request.EnterWaitingQueueRequest;
import java.util.UUID;

public interface WaitingQueueDomainService {
    WaitingQueueToken enterWaitingQueue(EnterWaitingQueueRequest request);
    WaitingQueueToken retrieveToken(UUID userId);
}
