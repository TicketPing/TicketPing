package com.ticketPing.queue_manage.domain.command.workingQueue;

import com.ticketPing.queue_manage.domain.model.enums.DeleteWorkingTokenCase;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class DeleteWorkingTokenCommand {

    private String performanceId;
    private String tokenValue;
    private DeleteWorkingTokenCase deleteCase;

    public static DeleteWorkingTokenCommand create(DeleteWorkingTokenCase deleteCase, String tokenValue) {
        return DeleteWorkingTokenCommand.builder()
                .performanceId(tokenValue.split(":")[1])
                .tokenValue(tokenValue)
                .deleteCase(deleteCase)
                .build();
    }

}
