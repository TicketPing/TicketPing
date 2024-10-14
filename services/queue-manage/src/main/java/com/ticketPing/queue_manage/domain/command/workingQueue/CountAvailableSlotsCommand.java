package com.ticketPing.queue_manage.domain.command.workingQueue;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class CountAvailableSlotsCommand {

    private String performanceId;

    public static CountAvailableSlotsCommand create(String performanceId) {
        return CountAvailableSlotsCommand.builder()
                .performanceId(performanceId)
                .build();
    }

}
