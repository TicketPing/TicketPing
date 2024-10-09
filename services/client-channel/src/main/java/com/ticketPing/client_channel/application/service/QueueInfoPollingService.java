package com.ticketPing.client_channel.application.service;

import com.ticketPing.client_channel.application.dto.GeneralTokenResponse;
import java.util.UUID;

public interface QueueInfoPollingService {
    GeneralTokenResponse getWaitingQueueInfo(UUID userId);
}
