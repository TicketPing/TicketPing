package com.ticketPing.queue_manage.application.dto.workingQueue;

import com.ticketPing.queue_manage.domain.model.WorkingQueueToken;
import common.dto.mapper.ObjectMapperBasedVoMapper;
import java.util.UUID;

public record WorkingQueueTokenResponse(
        UUID userId,
        String tokenValue,
        String tokenStatus) {

    public static WorkingQueueTokenResponse from(WorkingQueueToken token) {
        return ObjectMapperBasedVoMapper.convert(token, WorkingQueueTokenResponse.class);
    }

}
