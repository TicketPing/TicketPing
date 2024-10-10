package com.ticketPing.client_channel.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GeneralTokenResponse(
        UUID userId,
        String performanceName,
        String tokenValue,
        String tokenStatus,
        Long position,
        Long totalUsers,
        LocalDateTime validUntil
) {
}

