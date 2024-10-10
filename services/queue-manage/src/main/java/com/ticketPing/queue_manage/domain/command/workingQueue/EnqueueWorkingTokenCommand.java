package com.ticketPing.queue_manage.domain.command.workingQueue;

import static com.ticketPing.queue_manage.infrastructure.utils.YmlLoader.workingQueueTokenTTL;

import com.ticketPing.queue_manage.domain.model.WorkingQueueToken;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class EnqueueWorkingTokenCommand {

    private String performanceName;
    private String tokenValue;
    private String value;
    private long ttl;

    public static EnqueueWorkingTokenCommand create(WorkingQueueToken token) {
        return EnqueueWorkingTokenCommand.builder()
                .performanceName(token.getPerformanceName())
                .tokenValue(token.getTokenValue())
                .value("NA")
                .ttl(workingQueueTokenTTL()).build();
    }

}
