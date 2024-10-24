package com.ticketPing.queue_manage.domain.model;

import com.ticketPing.queue_manage.domain.model.enums.TokenStatus;

public interface QueueToken {
    String getUserId();
    String getPerformanceId();
    String getTokenValue();
    TokenStatus getTokenStatus();
}
