package com.ticketPing.queue_manage.domain.command.waitingQueue;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class DequeueFirstWaitingTokenCommand {

    private String performanceName;
    private String queueName;

    public static DequeueFirstWaitingTokenCommand create(String expiredTokenValue) {
        String performanceName = expiredTokenValue.split("_")[0];
        return DequeueFirstWaitingTokenCommand.builder()
                .performanceName(performanceName)
                .queueName(performanceName + "_Queue")
                .build();
    }

}
