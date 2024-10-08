package com.ticketPing.client_channel.application.client;

import com.ticketPing.client_channel.infrastructure.annotation.FeignClientAdapter;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import response.CommonResponse;

@FeignClientAdapter
@RequiredArgsConstructor
public class QueueManageClientAdapter {

    private final QueueManageClient queueManageClient;

    public CommonResponse getWaitingQueueInfo(UUID userId) {
        return queueManageClient.getWaitingQueueInfo(userId);
    }

}
