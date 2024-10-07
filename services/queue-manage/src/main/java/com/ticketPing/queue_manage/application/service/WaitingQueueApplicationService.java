package com.ticketPing.queue_manage.application.service;

import com.ticketPing.queue_manage.application.dto.EnterWaitingQueueResponse;
import com.ticketPing.queue_manage.presentaion.request.EnterWaitingQueueRequest;

public interface WaitingQueueApplicationService {
    EnterWaitingQueueResponse enterWaitingQueue(EnterWaitingQueueRequest request);
}
