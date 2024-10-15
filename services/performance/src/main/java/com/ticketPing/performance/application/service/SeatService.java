package com.ticketPing.performance.application.service;

import com.ticketPing.performance.application.dtos.OrderInfoResponse;
import com.ticketPing.performance.application.dtos.SeatResponse;
import com.ticketPing.performance.domain.entity.Schedule;
import com.ticketPing.performance.domain.entity.Seat;
import com.ticketPing.performance.domain.repository.SeatRepository;
import com.ticketPing.performance.infrastructure.service.RedisService;
import com.ticketPing.performance.presentation.cases.exception.SeatExceptionCase;
import common.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SeatService {
    private final SeatRepository seatRepository;
    private final RedisService redisService;  // TODO: 상위 서비스 만들어서 불러오기

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
    public List<SeatResponse> getAllScheduleSeats(UUID scheduleId) {
        Set<String> ids = redisService.getKeys("seat:" + scheduleId + ":*");
        return redisService.getValuesAsClass(ids.stream().toList(), SeatResponse.class);
    }

    @Transactional
    public void createSeatsCache(Schedule schedule) {
        List<Seat> seats = findSeatsByScheduleJoinSeatCost(schedule);

        // counter 생성
        redisService.setValue("counter:" + schedule.getId(), String.valueOf(seats.size()));

        // 좌석 캐싱
        String prefix = "seat:" + schedule.getId() + ":";
        Map<String, Object> seatMap = new HashMap<>();
        seats.forEach(seat -> {seatMap.put(prefix+seat.getId(), SeatResponse.of(seat));});
        redisService.setValues(seatMap);
    }

    @Transactional
    public List<Seat> findSeatsByScheduleJoinSeatCost(Schedule schedule) {
        return seatRepository.findByScheduleJoinSeatCost(schedule);
    }
}
