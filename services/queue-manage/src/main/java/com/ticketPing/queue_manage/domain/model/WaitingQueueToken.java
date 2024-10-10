package com.ticketPing.queue_manage.domain.model;

import static com.ticketPing.queue_manage.domain.utils.QueueTokenValueGenerator.generateTokenValue;

import com.ticketPing.queue_manage.domain.model.enums.TokenStatus;
import common.dto.mapper.ObjectMapperBasedVoMapper;
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

    private UUID userId;
    private String performanceName;
    private String tokenValue;
    private TokenStatus tokenStatus;

    private long position;
    private long totalUsers;

    public static WaitingQueueToken create(UUID userId, String performanceName) {
        return WaitingQueueToken.builder()
                .userId(userId)
                .performanceName(performanceName)
                .tokenValue(generateTokenValue(userId, performanceName))
                .tokenStatus(TokenStatus.WAITING)
                .build();
    }

    public static WaitingQueueToken tokenWithPosition(UUID userId, String performanceName, String tokenValue, long position, long totalUsers) {
        return WaitingQueueToken.builder()
                .userId(userId)
                .performanceName(performanceName)
                .tokenValue(tokenValue)
                .tokenStatus(TokenStatus.WAITING)
                .position(position)
                .totalUsers(totalUsers)
                .build();
    }

    public static WaitingQueueToken valueOf(String performanceName, String tokenValue) {
        return WaitingQueueToken.builder()
                .performanceName(performanceName)
                .tokenValue(tokenValue)
                .build();
    }

    public WorkingQueueToken toWorkingQueueToken() {
        return ObjectMapperBasedVoMapper.convert(this, WorkingQueueToken.class);
    }

}
