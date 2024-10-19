package com.ticketPing.queue_manage.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ticketPing.queue_manage.domain.model.WaitingQueueToken;
import com.ticketPing.queue_manage.domain.model.WorkingQueueToken;
import com.ticketPing.queue_manage.domain.model.enums.TokenStatus;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GeneralQueueTokenResponse(
        String userId,
        String performanceId,
        String tokenValue,
        TokenStatus tokenStatus,
        Long position,
        Long totalUsers,
        LocalDateTime validUntil
) {

    public static GeneralQueueTokenResponse from(WaitingQueueToken token) {
        return new GeneralQueueTokenResponse(
                token.getUserId(),
                token.getPerformanceId(),
                token.getTokenValue(),
                token.getTokenStatus(),
                token.getPosition() == 0 ? null : token.getPosition(),
                token.getTotalUsers() == 0 ? null : token.getTotalUsers(),
                null
        );
    }

    public static GeneralQueueTokenResponse from(WorkingQueueToken token) {
        return new GeneralQueueTokenResponse(
                token.getUserId(),
                token.getPerformanceId(),
                token.getTokenValue(),
                token.getTokenStatus(),
                null,
                null,
                token.getValidUntil()
        );
    }

}
