package com.ticketPing.queue_manage.application.dto;

import com.ticketPing.queue_manage.domain.model.WaitingQueueToken;
import common.dto.mapper.ObjectMapperBasedVoMapper;
import java.util.UUID;

public record WaitingQueueInfoResponse(
        UUID userId,
        String tokenValue,
        String status,
        long position,
        long totalUsers) {

    public static WaitingQueueInfoResponse from(WaitingQueueToken token) {
        return ObjectMapperBasedVoMapper.convert(token, WaitingQueueInfoResponse.class);
    }

}
