package com.ticketping.gateway.domain.repository;

public interface QueueCheckRepository {
    int getAvailableSeats(String performanceId);
    long getWaitingUsers(String performanceId);
    Boolean findWorkingToken(String tokenValue);
}
