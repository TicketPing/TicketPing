package com.ticketPing.queue_manage.domain.model;

import static com.ticketPing.queue_manage.domain.utils.QueueTokenValueGenerator.generateTokenValue;

import com.ticketPing.queue_manage.domain.model.enums.TokenStatus;
import java.time.LocalDateTime;
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

    private String userId;
    private String performanceId;
    private String tokenValue;
    private TokenStatus tokenStatus;

    private LocalDateTime validUntil;

    public static WorkingQueueToken create(String userId, String performanceId) {
        return WorkingQueueToken.builder()
                .userId(userId)
                .performanceId(performanceId)
                .tokenValue(generateTokenValue(userId, performanceId))
                .tokenStatus(TokenStatus.WORKING)
                .build();
    }

    public static WorkingQueueToken withValidUntil(String userId, String performanceId, String tokenValue, LocalDateTime validUntil) {
        return WorkingQueueToken.builder()
                .userId(userId)
                .performanceId(performanceId)
                .tokenValue(tokenValue)
                .tokenStatus(TokenStatus.WORKING)
                .validUntil(validUntil)
                .build();
    }

}
