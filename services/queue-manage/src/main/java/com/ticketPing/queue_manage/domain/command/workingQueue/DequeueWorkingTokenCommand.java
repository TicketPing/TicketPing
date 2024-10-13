package com.ticketPing.queue_manage.domain.command.workingQueue;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class DequeueWorkingTokenCommand {

    private String performanceId;
    private String tokenValue;

    public static DequeueWorkingTokenCommand create(String tokenValue) {
        return DequeueWorkingTokenCommand.builder()
                .performanceId(tokenValue.split(":")[1])
                .tokenValue(tokenValue)
                .build();
    }

}
