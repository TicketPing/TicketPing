package com.ticketPing.queue_manage.domain.model;

import static com.ticketPing.queue_manage.infrastructure.utils.ConfigHolder.workingQueueMaxSize;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
public class AvailableSlots {

    private long count;

    public static AvailableSlots from(long currentCount) {
     return AvailableSlots.builder()
                .count(workingQueueMaxSize() - currentCount)
                .build();
    }

    public boolean isLimited() {
        return count <= 0;
    }

}
