package com.ticketPing.queue_manage.domain.command.workingQueue;

import static com.ticketPing.queue_manage.infrastructure.utils.ConfigHolder.workingQueueTokenTTL;

import com.ticketPing.queue_manage.domain.model.WorkingQueueToken;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class InsertWorkingTokenCommand {

    private String performanceId;
    private String tokenValue;
    private String value;
    private long ttl;

    public static InsertWorkingTokenCommand create(WorkingQueueToken token) {
        return InsertWorkingTokenCommand.builder()
                .performanceId(token.getPerformanceId())
                .tokenValue(token.getTokenValue())
                .value("NA")
                .ttl(workingQueueTokenTTL()).build();
    }

}
