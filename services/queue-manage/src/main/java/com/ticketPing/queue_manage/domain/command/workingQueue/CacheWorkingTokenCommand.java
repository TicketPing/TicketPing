package com.ticketPing.queue_manage.domain.command.workingQueue;

import static com.ticketPing.queue_manage.infrastructure.utils.YmlLoader.workingQueueTokenTTL;

import com.ticketPing.queue_manage.domain.model.WorkingQueueToken;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class CacheWorkingTokenCommand {

    private String tokenValue;
    private String value;
    private long ttl;

    public static CacheWorkingTokenCommand create(WorkingQueueToken workingQueueToken) {
        return CacheWorkingTokenCommand.builder()
                .tokenValue(workingQueueToken.getTokenValue())
                .value("NA")
                .ttl(workingQueueTokenTTL()).build();
    }

}
