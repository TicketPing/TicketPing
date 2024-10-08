package com.ticketPing.queue_manage.application.dto;

import com.ticketPing.queue_manage.application.dto.mapper.ObjectMapperBasedVoMapper;
import com.ticketPing.queue_manage.domain.model.WaitingQueueToken;
import java.util.UUID;

public record EnterWaitingQueueResponse(
        UUID userId,
        String tokenValue,
        String status) {

    public static EnterWaitingQueueResponse from(WaitingQueueToken token) {
        return ObjectMapperBasedVoMapper.convert(token, EnterWaitingQueueResponse.class);
    }

}