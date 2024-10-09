package com.ticketPing.queue_manage.application.service;

import com.ticketPing.queue_manage.application.dto.waitingQueue.EnterWaitingQueueResponse;
import com.ticketPing.queue_manage.application.dto.waitingQueue.WaitingQueueInfoResponse;
import com.ticketPing.queue_manage.presentaion.request.EnterWaitingQueueRequest;
import java.util.UUID;

public interface WaitingQueueApplicationService {
    EnterWaitingQueueResponse enterWaitingQueue(EnterWaitingQueueRequest request);
    WaitingQueueInfoResponse getWaitingQueueInfo(UUID userId);
}
