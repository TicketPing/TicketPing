package com.ticketPing.queue_manage.domain.model;

import com.ticketPing.queue_manage.domain.model.enums.TokenStatus;
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
    private String tokenValue;
    private TokenStatus tokenStatus;

    public WorkingQueueToken withWorking() {
        this.tokenStatus = TokenStatus.WORKING;
        return this;
    }

    public static WorkingQueueToken valueOf(UUID userId, String tokenValue) {
        return WorkingQueueToken.builder()
                .userId(userId)
                .tokenValue(tokenValue)
                .tokenStatus(TokenStatus.WORKING)
                .build();
    }

}
