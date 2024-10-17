package com.ticketPing.queue_manage.domain.command.waitingQueue;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class DeleteFirstWaitingTokenCommand {

    private String performanceId;
    private String queueName;

    public static DeleteFirstWaitingTokenCommand create(String expiredTokenValue) {
        String performanceId = expiredTokenValue.split(":")[1];
        return DeleteFirstWaitingTokenCommand.builder()
                .performanceId(performanceId)
                .queueName(performanceId + "_Queue")
                .build();
    }

}
