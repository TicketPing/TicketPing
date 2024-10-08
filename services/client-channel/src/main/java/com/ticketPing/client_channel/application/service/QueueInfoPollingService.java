package com.ticketPing.client_channel.application.service;

import com.ticketPing.client_channel.application.dto.WaitingQueueInfoResponse;
import java.util.UUID;

public interface QueueInfoPollingService {
    WaitingQueueInfoResponse getWaitingQueueInfo(UUID userId);
}
