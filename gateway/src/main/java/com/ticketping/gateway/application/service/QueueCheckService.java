package com.ticketping.gateway.application.service;

public interface QueueCheckService {
    boolean areTooManyWaitingUsers(String performanceId);
    boolean isSoldOut(String performanceId);
    boolean isExistToken(String userId, String performanceId);
}
