package com.ticketPing.queue_manage.domain.model;

import com.ticketPing.queue_manage.domain.model.enums.QueueStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
public class WorkingQueueToken {

    private String tokenValue;
    private QueueStatus status;

    public WorkingQueueToken withWorking() {
        this.status = QueueStatus.WORKING;
        return this;
    }

}
