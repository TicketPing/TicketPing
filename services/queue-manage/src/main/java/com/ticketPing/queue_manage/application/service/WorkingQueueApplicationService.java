package com.ticketPing.queue_manage.application.service;

import com.ticketPing.queue_manage.application.dto.workingQueue.WorkingQueueTokenResponse;
import java.util.UUID;

public interface WorkingQueueApplicationService {
    void processQueueTransfer();
    WorkingQueueTokenResponse getWorkingQueueToken(UUID userId);
}
