package com.ticketPing.queue_manage.application.dto;

import com.ticketPing.queue_manage.application.mapper.ObjectMapperBasedVoMapper;
import com.ticketPing.queue_manage.domain.model.QueueStatus;
import com.ticketPing.queue_manage.domain.model.WaitingQueueToken;
import java.util.UUID;

public record WaitingQueueInfoResponse(
        UUID userId,
        String tokenValue,
        QueueStatus status,
        long position,
        long totalUsers
        ) {

    public static WaitingQueueInfoResponse from(WaitingQueueToken token) {
        return ObjectMapperBasedVoMapper.convert(token, WaitingQueueInfoResponse.class);
    }

}
