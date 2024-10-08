package com.ticketPing.queue_manage.domain.command.workingQueue;

import static com.ticketPing.queue_manage.infrastructure.utils.YmlLoader.workingQueueTokenTtl;

import com.ticketPing.queue_manage.domain.model.WorkingQueueToken;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class CacheTokenCommand {

    private String user;
    private String value;
    private long ttl;

    public static CacheTokenCommand create(WorkingQueueToken workingQueueToken) {
        return CacheTokenCommand.builder()
                .user(workingQueueToken.getTokenValue())
                .value("NA")
                .ttl(workingQueueTokenTtl()).build();
    }

}
