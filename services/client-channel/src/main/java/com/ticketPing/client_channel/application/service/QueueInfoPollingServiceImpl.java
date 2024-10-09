package com.ticketPing.client_channel.application.service;

import com.ticketPing.client_channel.application.client.QueueManageClientAdapter;
import com.ticketPing.client_channel.application.dto.GeneralTokenResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import common.response.CommonResponse;

@Service
@RequiredArgsConstructor
public class QueueInfoPollingServiceImpl implements QueueInfoPollingService {

    private final QueueManageClientAdapter queueManageClient;

    public GeneralTokenResponse getWaitingQueueInfo(UUID userId) {
        CommonResponse<GeneralTokenResponse> response = queueManageClient.getWaitingQueueInfo(userId);
        return response.getData();
    }

}
