package com.ticketPing.client_channel.application.dto;

import java.util.UUID;

public record WaitingQueueInfoResponse(
        UUID userId,
        String tokenValue,
        String status,
        long position,
        long totalUsers) {
}
