package com.ticketPing.queue_manage.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ticketPing.queue_manage.domain.model.WaitingQueueToken;
import com.ticketPing.queue_manage.domain.model.WorkingQueueToken;
import com.ticketPing.queue_manage.domain.model.enums.TokenStatus;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GeneralTokenResponse(
        UUID userId,
        String performanceName,
        String tokenValue,
        TokenStatus tokenStatus,
        Long position,
        Long totalUsers,
        LocalDateTime validUntil
) {

    public static GeneralTokenResponse from(WaitingQueueToken token) {
        return new GeneralTokenResponse(
                token.getUserId(),
                token.getPerformanceName(),
                token.getTokenValue(),
                token.getTokenStatus(),
                token.getPosition() == 0 ? null : token.getPosition(),
                token.getTotalUsers() == 0 ? null : token.getTotalUsers(),
                null
        );
    }

    public static GeneralTokenResponse from(WorkingQueueToken token) {
        return new GeneralTokenResponse(
                token.getUserId(),
                token.getPerformanceName(),
                token.getTokenValue(),
                token.getTokenStatus(),
                null,
                null,
                token.getValidUntil()
        );
    }

}
