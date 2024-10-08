package com.ticketPing.queue_manage.domain.model;

import static com.ticketPing.queue_manage.domain.utils.QueueTokenValueGenerator.generateTokenValue;

import com.ticketPing.queue_manage.application.dto.mapper.ObjectMapperBasedVoMapper;
import com.ticketPing.queue_manage.domain.model.enums.QueueStatus;
import com.ticketPing.queue_manage.presentaion.request.EnterWaitingQueueRequest;
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
    private String tokenValue;
    private QueueStatus status;

    private long position;
    private long totalUsers;

    public static WaitingQueueToken create(EnterWaitingQueueRequest request) {
        return WaitingQueueToken.builder()
                .userId(request.userId())
                .tokenValue(generateTokenValue(request.userId()))
                .status(QueueStatus.WAITING)
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

    public static WaitingQueueToken valueOf(String tokenValue) {
        return WaitingQueueToken.builder().tokenValue(tokenValue).build();
    }

    public WorkingQueueToken toWorkingQueueToken(){
        return ObjectMapperBasedVoMapper.convert(this, WorkingQueueToken.class).withWorking();
    }

}
