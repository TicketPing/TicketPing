package com.ticketPing.order.domain.events;


public record OrderCompletedEvent(
        String userId,
        String performanceId
) {

    public static OrderCompletedEvent create(String userId, String performanceId) {
        return new OrderCompletedEvent(
                userId,
                performanceId
        );
    }

}
