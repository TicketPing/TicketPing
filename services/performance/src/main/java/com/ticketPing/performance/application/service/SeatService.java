package com.ticketPing.performance.application.service;

import com.ticketPing.performance.application.dtos.SeatResponse;
import com.ticketPing.performance.domain.entity.Seat;
import com.ticketPing.performance.domain.repository.SeatRepository;
import com.ticketPing.performance.presentation.cases.exception.SeatExceptionCase;
import common.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeatService {
    private final SeatRepository seatRepository;

    @Transactional(readOnly = true)
    public SeatResponse getSeat(UUID id) {
        Seat seat = findSeatById(id);
        return SeatResponse.of(seat);
    }

    @Transactional
    public Seat findSeatById(UUID id) {
        return seatRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(SeatExceptionCase.SEAT_NOT_FOUND));
    }
}
