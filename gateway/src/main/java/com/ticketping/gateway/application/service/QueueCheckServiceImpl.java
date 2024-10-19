package com.ticketping.gateway.application.service;

import static com.ticketping.gateway.domain.utils.QueueTokenValueGenerator.generateTokenValue;

import com.ticketping.gateway.domain.repository.QueueCheckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueueCheckServiceImpl implements QueueCheckService {

    private final QueueCheckRepository queueCheckRepository;

    // 공연 잔여석 * 2 <= 대기열 인원 수
    @Override
    public boolean areTooManyWaitingUsers(String performanceId) {
        return queueCheckRepository.getAvailableSeats(performanceId) * 2 <= queueCheckRepository.getWaitingUsers(performanceId);
    }

    // 매진 여부
    @Override
    public boolean isSoldOut(String performanceId) {
        return queueCheckRepository.getAvailableSeats(performanceId) <= 0;
    }

    // 작업열 토큰 조회
    @Override
    public boolean isExistToken(String userId, String performanceId) {
        String tokenValue = generateTokenValue(userId, performanceId);
        return queueCheckRepository.findWorkingToken(tokenValue) == true;
    }

}