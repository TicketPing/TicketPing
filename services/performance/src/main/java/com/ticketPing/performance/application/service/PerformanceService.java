package com.ticketPing.performance.application.service;

import com.ticketPing.performance.application.dtos.PerformanceResponse;
import com.ticketPing.performance.domain.entity.Performance;
import com.ticketPing.performance.domain.repository.PerformanceRepository;
import com.ticketPing.performance.presentation.cases.exception.PerformanceExceptionCase;
import common.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class PerformanceService {

    private final PerformanceRepository performanceRepository;

    @Transactional(readOnly = true)
    public PerformanceResponse getPerformance(UUID id) {
        Performance performance = findPerformanceById(id);
        return PerformanceResponse.of(performance);
    }

    @Transactional
    public Performance findPerformanceById(UUID id) {
        return performanceRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(PerformanceExceptionCase.PERFORMANCE_NOT_FOUND));
    }

//
//    public List<PerformanceResponseDto> getAllPerformances() {
//        List<Performance> performances = performanceRepository.findAll();
//
//        return performances.stream().map(
//            PerformanceResponseDto::of).toList();
//    }
//
//    public List<PerformanceScheduleResponseDto> getAllPerformancesByDate(LocalDate localDate) {
//        List<Schedule> performanceScheduleList = performanceScheduleRepository.findAllByScheduledDate(
//            localDate);
//
//        return performanceScheduleList.stream().map(PerformanceScheduleResponseDto::of).toList();
//    }
//
//    public List<OrderPerformanceDto> findByPerformanceHallId(UUID performanceHallId) {
//        // 공연장에 해당하는 좌석 정보를 가져옴
//        List<HallSeats> hallSeats = hallSeatsRepository.findByPerformanceHallId(performanceHallId);
//        PerformanceSchedule performanceSchedule = performanceScheduleRepository.findByPerformanceHallId(
//                performanceHallId)
//            .orElseThrow(() -> new RuntimeException(PERFORMANCE_SCHEDULE_NOT_FOUND.getMessage()));
//
//        UUID scheduleId = performanceSchedule.getId();
//        UUID companyId = performanceSchedule.getPerformance().getCompanyId();
//        String performanceName = performanceSchedule.getPerformance().getName();
//
//        // HallSeats 엔티티를 HallSeatsDto로 변환하여 리스트로 반환
//        return hallSeats.stream()
//            .map(hallSeat -> convertToDto(hallSeat, scheduleId, companyId, performanceName))
//            .collect(Collectors.toList());
//    }
//
//    // HallSeats 엔티티를 HallSeatsDto로 변환하는 메서드
//    private OrderPerformanceDto convertToDto(HallSeats hallSeat, UUID scheduleId, UUID companyId, String performanceName) {
//        return OrderPerformanceDto.builder()
//            .seatRow(hallSeat.getSeatRow())
//            .seatColumn(hallSeat.getSeatColumn())
//            .price(hallSeat.getPrice())
//            .seatRate(hallSeat.getSeatRate().name()) // SeatRate를 String으로 변환
//            .performanceHall(hallSeat.getPerformanceHall())
//            .startTime(hallSeat.getStartTime())
//            .endTime(hallSeat.getEndTime())
//            .performanceDate(hallSeat.getPerformanceDate())
//            .totalSeats(hallSeat.getTotalSeats())
//            .performanceHallId(hallSeat.getPerformanceHallId())
//            .orderCancelled(false)
//            .reservationDate(null)
//            .userId(null)
//            .scheduleId(scheduleId)
//            .companyId(companyId)
//            .performanceName(performanceName)
//            .build();
//    }
}

