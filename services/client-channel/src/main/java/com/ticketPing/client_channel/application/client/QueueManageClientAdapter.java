package com.ticketPing.client_channel.application.client;

import com.ticketPing.client_channel.application.dto.GeneralTokenResponse;
import com.ticketPing.client_channel.infrastructure.annotation.FeignClientAdapter;
import com.ticketPing.client_channel.infrastructure.client.QueueManageClient;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import common.response.CommonResponse;

@FeignClientAdapter
@RequiredArgsConstructor
public class QueueManageClientAdapter {

    private final QueueManageClient queueManageClient;

    public CommonResponse<GeneralTokenResponse> getWaitingQueueInfo(UUID userId, String performanceName) {
        return queueManageClient.getWaitingQueueInfo(userId, performanceName);
    }

}
