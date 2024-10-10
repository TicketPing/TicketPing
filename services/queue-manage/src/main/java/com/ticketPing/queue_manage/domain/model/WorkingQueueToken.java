package com.ticketPing.queue_manage.domain.model;

import static com.ticketPing.queue_manage.domain.utils.QueueTokenValueGenerator.generateTokenValue;

import com.ticketPing.queue_manage.domain.model.enums.TokenStatus;
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
public class WorkingQueueToken {

    private UUID userId;
    private String performanceName;
    private String tokenValue;
    private TokenStatus tokenStatus;

    private LocalDateTime validUntil;

    public static WorkingQueueToken create(UUID userId, String performanceName) {
        return WorkingQueueToken.builder()
                .userId(userId)
                .performanceName(performanceName)
                .tokenValue(generateTokenValue(userId, performanceName))
                .tokenStatus(TokenStatus.WORKING)
                .build();
    }

    public static WorkingQueueToken tokenWithValidUntil(UUID userId, String performanceName, String tokenValue, LocalDateTime validUntil
    ) {
        return WorkingQueueToken.builder()
                .userId(userId)
                .performanceName(performanceName)
                .tokenValue(tokenValue)
                .tokenStatus(TokenStatus.WORKING)
                .validUntil(validUntil)
                .build();
    }

}
