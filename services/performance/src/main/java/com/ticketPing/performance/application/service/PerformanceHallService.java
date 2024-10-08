package com.ticketPing.performance.application.service;

import static com.ticketPing.performance.presentation.response.exception.PerformanceExceptionCase.PERFORMANCE_HALL_NOT_FOUND;

import com.ticketPing.performance.application.dtos.PerformanceHallResponseDto;
import com.ticketPing.performance.domain.entity.PerformanceHall;
import com.ticketPing.performance.domain.repository.PerformanceHallRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PerformanceHallService {

    private final PerformanceHallRepository hallRepository;

    public PerformanceHallResponseDto getPerformanceHall(UUID id) {
        PerformanceHall performanceHall = hallRepository.findById(id)
            .orElseThrow(() -> new RuntimeException(PERFORMANCE_HALL_NOT_FOUND.getMessage()));

        return PerformanceHallResponseDto.of(performanceHall);
    }

    public List<PerformanceHallResponseDto> getAllPerformanceHalls() {
        List<PerformanceHall> performanceHallList = hallRepository.findAll();

        return performanceHallList.stream().map(PerformanceHallResponseDto::of).toList();
    }

    public String findPerformanceHallName(UUID performanceHallId) {
        PerformanceHall performanceHall = hallRepository.findById(performanceHallId)
            .orElseThrow(()-> new RuntimeException(PERFORMANCE_HALL_NOT_FOUND.getMessage()));

        return performanceHall.getName();
    }
}

