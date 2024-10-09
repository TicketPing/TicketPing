package com.ticketPing.queue_manage.domain.command.workingQueue;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class CountAvailableSlotsCommand {

    private String performanceName;

    public static CountAvailableSlotsCommand create(String performanceName) {
        return CountAvailableSlotsCommand.builder()
                .performanceName(performanceName)
                .build();
    }

}
