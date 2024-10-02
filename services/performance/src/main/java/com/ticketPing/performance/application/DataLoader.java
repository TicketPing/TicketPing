package com.ticketPing.performance.application;

import com.ticketPing.performance.domain.entity.Performance;
import com.ticketPing.performance.domain.entity.PerformanceHall;
import com.ticketPing.performance.domain.entity.PerformanceSchedule;
import com.ticketPing.performance.domain.entity.SeatCost;
import com.ticketPing.performance.domain.entity.SeatRate;
import com.ticketPing.performance.domain.repository.PerformanceHallRepository;
import com.ticketPing.performance.domain.repository.PerformanceRepository;
import com.ticketPing.performance.domain.repository.PerformanceScheduleRepository;
import com.ticketPing.performance.domain.repository.SeatCostRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final PerformanceRepository performanceRepository;
    private final PerformanceHallRepository performanceHallRepository;
    private final PerformanceScheduleRepository performanceScheduleRepository;
    private final SeatCostRepository seatCostRepository;

    public DataLoader(PerformanceRepository performanceRepository,
        PerformanceHallRepository performanceHallRepository,
        PerformanceScheduleRepository performanceScheduleRepository,
        SeatCostRepository seatCostRepository) {
        this.performanceRepository = performanceRepository;
        this.performanceHallRepository = performanceHallRepository;
        this.performanceScheduleRepository = performanceScheduleRepository;
        this.seatCostRepository = seatCostRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 이미 공연장 데이터가 존재하는지 확인
        if (performanceHallRepository.count() > 0) {
            return; // 데이터가 이미 존재하면 메서드 종료
        }

        // 공연장 더미 데이터 생성
        PerformanceHall hall1 = new PerformanceHall("국립극장", "서울특별시 남산동 1-1", 800, 30, 30);
        performanceHallRepository.save(hall1);

        PerformanceHall hall2 = new PerformanceHall("세종문화회관", "서울특별시 세종로 81", 1200, 40, 30);
        performanceHallRepository.save(hall2);

        // 공연 더미 데이터 생성
        Performance performance1 = new Performance("햄릿",
            LocalDateTime.now().minusDays(10),
            LocalDateTime.now().plusDays(5),
            LocalDate.now().minusDays(5),
            LocalDate.now().plusDays(10),
            "A",
            UUID.randomUUID());
        performanceRepository.save(performance1);

        Performance performance2 = new Performance("라이온 킹",
            LocalDateTime.now().minusDays(5),
            LocalDateTime.now().plusDays(15),
            LocalDate.now().minusDays(3),
            LocalDate.now().plusDays(12),
            "S",
            UUID.randomUUID());
        performanceRepository.save(performance2);

        // 공연 일정 더미 데이터 생성
        PerformanceSchedule schedule1 = new PerformanceSchedule(
            LocalTime.of(19, 0),
            LocalTime.of(21, 0),
            hall1,
            performance1,
            LocalDate.now().plusDays(1) // scheduledDate 추가
        );
        performanceScheduleRepository.save(schedule1);

        PerformanceSchedule schedule2 = new PerformanceSchedule(
            LocalTime.of(15, 0),
            LocalTime.of(17, 0),
            hall2,
            performance2,
            LocalDate.now().plusDays(2) // scheduledDate 추가
        );
        performanceScheduleRepository.save(schedule2);

        // 좌석 가격 더미 데이터 생성
        SeatCost seatCost1 = new SeatCost(SeatRate.S, 120000, performance1); // 가격을 원화로 변경
        seatCostRepository.save(seatCost1);

        SeatCost seatCost2 = new SeatCost(SeatRate.A, 90000, performance1);
        seatCostRepository.save(seatCost2);

        SeatCost seatCost3 = new SeatCost(SeatRate.B, 60000, performance1);
        seatCostRepository.save(seatCost3);

        SeatCost seatCost4 = new SeatCost(SeatRate.S, 150000, performance2);
        seatCostRepository.save(seatCost4);

        SeatCost seatCost5 = new SeatCost(SeatRate.A, 110000, performance2);
        seatCostRepository.save(seatCost5);

        SeatCost seatCost6 = new SeatCost(SeatRate.B, 80000, performance2);
        seatCostRepository.save(seatCost6);
    }
}
