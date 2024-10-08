package com.ticketPing.client_channel.application.service;

import com.ticketPing.client_channel.application.client.QueueManageClientAdapter;
import com.ticketPing.client_channel.application.dto.WaitingQueueInfoResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import common.response.CommonResponse;

@Service
@RequiredArgsConstructor
public class QueueInfoPollingServiceImpl implements QueueInfoPollingService {

    private final QueueManageClientAdapter queueManageClient;

    public WaitingQueueInfoResponse getWaitingQueueInfo(UUID userId) {
        CommonResponse<WaitingQueueInfoResponse> response = queueManageClient.getWaitingQueueInfo(userId);
        return response.getData();
    }

}
