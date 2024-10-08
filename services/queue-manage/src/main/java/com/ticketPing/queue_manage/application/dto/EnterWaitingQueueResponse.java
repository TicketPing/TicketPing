package com.ticketPing.queue_manage.application.dto;

import com.ticketPing.queue_manage.domain.model.WaitingQueueToken;
import common.dto.mapper.ObjectMapperBasedVoMapper;
import java.util.UUID;

public record EnterWaitingQueueResponse(
        UUID userId,
        String tokenValue,
        String status) {

    public static EnterWaitingQueueResponse from(WaitingQueueToken token) {
        return ObjectMapperBasedVoMapper.convert(token, EnterWaitingQueueResponse.class);
    }

}