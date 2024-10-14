package com.ticketPing.queue_manage.domain.events;

public record OrderCompletedEvent(String userId, String performanceId) {
}
