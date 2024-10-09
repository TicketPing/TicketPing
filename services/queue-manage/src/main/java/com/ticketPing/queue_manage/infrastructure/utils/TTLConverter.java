package com.ticketPing.queue_manage.infrastructure.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TTLConverter {
    public static LocalDateTime toLocalDateTime(long ttlInMilliseconds) {
        Instant instant = Instant.now().plusMillis(ttlInMilliseconds);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}
