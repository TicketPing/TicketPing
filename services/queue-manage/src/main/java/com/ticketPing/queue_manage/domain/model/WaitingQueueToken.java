package com.ticketPing.queue_manage.domain.model;

import static com.ticketPing.queue_manage.domain.utils.QueueTokenValueGenerator.generateTokenValue;

import com.ticketPing.queue_manage.presentaion.request.EnterWaitingQueueRequest;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
public class WaitingQueueToken {

    private int tokenId;
    private UUID userId;
    private String tokenValue;
    private QueueStatus status;
    private LocalDateTime validUntil;

    private long position;
    private long totalUsers;

    public static WaitingQueueToken create(EnterWaitingQueueRequest request) {
        return WaitingQueueToken.builder()
                .userId(request.userId())
                .tokenValue(generateTokenValue(request.userId()))
                .status(QueueStatus.WAITING)
                .validUntil(LocalDateTime.now().plusMinutes(60))
                .build();
    }

    public static WaitingQueueToken tokenWithPosition(UUID userId, String tokenValue, long position, long totalUsers) {
        return WaitingQueueToken.builder()
                .userId(userId)
                .tokenValue(tokenValue)
                .status(QueueStatus.WAITING)
                .position(position)
                .totalUsers(totalUsers)
                .build();
    }

}
