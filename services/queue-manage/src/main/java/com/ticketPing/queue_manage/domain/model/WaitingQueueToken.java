package com.ticketPing.queue_manage.domain.model;

import static com.ticketPing.queue_manage.domain.utils.QueueTokenValueGenerator.generateTokenValue;

import com.ticketPing.queue_manage.domain.model.enums.TokenStatus;
import common.dto.mapper.ObjectMapperBasedVoMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
public class WaitingQueueToken implements QueueToken {

    private String userId;
    private String performanceId;
    private String tokenValue;
    private TokenStatus tokenStatus;

    private long position;
    private long totalUsers;

    public static WaitingQueueToken create(String userId, String performanceId) {
        return WaitingQueueToken.builder()
                .userId(userId)
                .performanceId(performanceId)
                .tokenValue(generateTokenValue(userId, performanceId))
                .tokenStatus(TokenStatus.WAITING)
                .build();
    }

    public static WaitingQueueToken withPosition(String userId, String performanceId, String tokenValue, long position, long totalUsers) {
        return WaitingQueueToken.builder()
                .userId(userId)
                .performanceId(performanceId)
                .tokenValue(tokenValue)
                .tokenStatus(TokenStatus.WAITING)
                .position(position)
                .totalUsers(totalUsers)
                .build();
    }

    public static WaitingQueueToken valueOf(String performanceId, String tokenValue) {
        return WaitingQueueToken.builder()
                .performanceId(performanceId)
                .tokenValue(tokenValue)
                .build();
    }

    public WorkingQueueToken toWorkingQueueToken() {
        return ObjectMapperBasedVoMapper.convert(this, WorkingQueueToken.class);
    }

}
