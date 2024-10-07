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

    private Long tokenId;
    private UUID userId;
    private String tokenValue;
    private Long position;
    private LocalDateTime validUntil;
    private QueueStatus status;

    public static WaitingQueueToken create(EnterWaitingQueueRequest request) {
        return WaitingQueueToken.builder()
                .userId(request.userId())
                .tokenValue(generateTokenValue(request.userId()))
                .status(QueueStatus.WAITING)
                .validUntil(LocalDateTime.now().plusMinutes(60))
                .build();
    }

    public WaitingQueueToken withPositionValue(Long position) {
        this.position= position;
        return this;
    }

    public WaitingQueueToken calculatePosition(Long minTokenId) {
        Long position = this.tokenId - minTokenId + 1;
        return this.withPositionValue(position);
    }

}
