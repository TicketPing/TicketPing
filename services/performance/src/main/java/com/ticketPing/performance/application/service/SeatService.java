package com.ticketPing.performance.application.service;

import com.ticketPing.performance.application.dtos.OrderInfoResponse;
import com.ticketPing.performance.application.dtos.SeatResponse;
import com.ticketPing.performance.domain.entity.RedisSeat;
import com.ticketPing.performance.domain.entity.Schedule;
import com.ticketPing.performance.domain.entity.Seat;
import com.ticketPing.performance.domain.repository.SeatRepository;
import com.ticketPing.performance.infrastructure.repository.RedisSeatRepository;
import com.ticketPing.performance.presentation.cases.exception.SeatExceptionCase;
import common.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeatService {
    private final SeatRepository seatRepository;
    // TODO: domain에 상위 레포지토리 만들기 (saveAll이 오버라이딩이 계속 안됨)
    private final RedisSeatRepository redisSeatRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional(readOnly = true)
    public SeatResponse getSeat(UUID id) {
        Seat seat = findSeatByIdJoinSeatCost(id);
        return SeatResponse.of(seat);
    }

    @Transactional
    public SeatResponse updateSeatState(UUID seatId, Boolean seatState) {
        Seat seat = findSeatByIdJoinSeatCost(seatId);
        seat.updateSeatState(seatState);
        return SeatResponse.of(seat);
    }

    @Transactional
    public Seat findSeatByIdJoinSeatCost(UUID id) {
        return seatRepository.findByIdJoinSeatCost(id)
                .orElseThrow(() -> new ApplicationException(SeatExceptionCase.SEAT_NOT_FOUND));
    }

    @Transactional
    public OrderInfoResponse getOrderInfo(UUID seatId) {
        Seat seat = seatRepository.findByIdJoinAll(seatId)
                .orElseThrow(() -> new ApplicationException(SeatExceptionCase.SEAT_NOT_FOUND));
        return OrderInfoResponse.of(seat);
    }

    @Transactional
    public void createSeatsCache(Schedule schedule) {
        List<Seat> seats = findSeatsByScheduleJoinSeatCost(schedule);

        // counter 생성
        redisTemplate.opsForValue().set("counter:" + schedule.getId(), seats.size());

        // 좌석 캐싱
        Iterable<RedisSeat> redisSeats = seats.stream().map(RedisSeat::from).toList();
        redisSeatRepository.saveAll(redisSeats);
    }

    @Transactional
    public List<Seat> findSeatsByScheduleJoinSeatCost(Schedule schedule) {
        return seatRepository.findByScheduleJoinSeatCost(schedule);
    }
}
