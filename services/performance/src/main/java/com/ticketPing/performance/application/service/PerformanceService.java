package com.ticketPing.performance.application.service;

import static com.ticketPing.performance.presentation.response.exception.PerformanceExceptionCase.PERFORMANCE_SCHEDULE_NOT_FOUND;

import dto.OrderPerformanceDto;
import com.ticketPing.performance.application.dtos.PerformanceResponseDto;
import com.ticketPing.performance.application.dtos.PerformanceScheduleResponseDto;
import com.ticketPing.performance.domain.entity.HallSeats;
import com.ticketPing.performance.domain.entity.Performance;
import com.ticketPing.performance.domain.entity.PerformanceSchedule;
import com.ticketPing.performance.domain.repository.HallSeatsRepository;
import com.ticketPing.performance.domain.repository.PerformanceRepository;
import com.ticketPing.performance.domain.repository.PerformanceScheduleRepository;
import com.ticketPing.performance.presentation.response.exception.PerformanceExceptionCase;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PerformanceService {

    private final PerformanceRepository performanceRepository;
    private final PerformanceScheduleRepository performanceScheduleRepository;
    private final HallSeatsRepository hallSeatsRepository;

    public PerformanceResponseDto getPerformance(UUID id) {
        Performance performance = performanceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException(
                PerformanceExceptionCase.PERFORMANCE_NOT_FOUND.getMessage()));
        return PerformanceResponseDto.of(performance);
    }

    public List<PerformanceResponseDto> getAllPerformances() {
        List<Performance> performances = performanceRepository.findAll();

        return performances.stream().map(
            PerformanceResponseDto::of).toList();
    }

    public List<PerformanceScheduleResponseDto> getAllPerformancesByDate(LocalDate localDate) {
        List<PerformanceSchedule> performanceScheduleList = performanceScheduleRepository.findAllByScheduledDate(
            localDate);

        return performanceScheduleList.stream().map(PerformanceScheduleResponseDto::of).toList();
    }

    public List<OrderPerformanceDto> findByPerformanceHallId(UUID performanceHallId) {
        // 공연장에 해당하는 좌석 정보를 가져옴
        List<HallSeats> hallSeats = hallSeatsRepository.findByPerformanceHallId(performanceHallId);
        PerformanceSchedule performanceSchedule = performanceScheduleRepository.findByPerformanceHallId(performanceHallId)
            .orElseThrow(() -> new RuntimeException(PERFORMANCE_SCHEDULE_NOT_FOUND.getMessage()));

        UUID scheduleId = performanceSchedule.getId();
        UUID companyId = performanceSchedule.getPerformance().getCompanyId();

        // HallSeats 엔티티를 HallSeatsDto로 변환하여 리스트로 반환
        return hallSeats.stream()
            .map(hallSeat -> convertToDto(hallSeat, scheduleId, companyId))
            .collect(Collectors.toList());
    }

    // HallSeats 엔티티를 HallSeatsDto로 변환하는 메서드
    private OrderPerformanceDto convertToDto(HallSeats hallSeat, UUID scheduleId, UUID companyId) {
        return OrderPerformanceDto.builder()
            .seatRow(hallSeat.getSeatRow())
            .seatColumn(hallSeat.getSeatColumn())
            .price(hallSeat.getPrice())
            .seatRate(hallSeat.getSeatRate().name()) // SeatRate를 String으로 변환
            .performanceHall(hallSeat.getPerformanceHall())
            .startTime(hallSeat.getStartTime())
            .endTime(hallSeat.getEndTime())
            .performanceDate(hallSeat.getPerformanceDate())
            .totalSeats(hallSeat.getTotalSeats())
            .performanceHallId(hallSeat.getPerformanceHallId())
            .orderCancelled(false)
            .reservationDate(null)
            .userId(null)
            .scheduleId(scheduleId)
            .companyId(companyId)
            .build();
    }

}

