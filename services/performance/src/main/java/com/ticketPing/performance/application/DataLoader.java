package com.ticketPing.performance.application;

import com.ticketPing.performance.domain.entity.HallSeats;
import com.ticketPing.performance.domain.entity.Performance;
import com.ticketPing.performance.domain.entity.PerformanceHall;
import com.ticketPing.performance.domain.entity.PerformanceSchedule;
import com.ticketPing.performance.domain.entity.SeatCost;
import com.ticketPing.performance.domain.entity.SeatRate;
import com.ticketPing.performance.domain.repository.HallSeatsRepository;
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
    private final HallSeatsRepository hallSeatsRepository;

    public DataLoader(PerformanceRepository performanceRepository,
        PerformanceHallRepository performanceHallRepository,
        PerformanceScheduleRepository performanceScheduleRepository,
        SeatCostRepository seatCostRepository,
        HallSeatsRepository hallSeatsRepository) {
        this.performanceRepository = performanceRepository;
        this.performanceHallRepository = performanceHallRepository;
        this.performanceScheduleRepository = performanceScheduleRepository;
        this.seatCostRepository = seatCostRepository;
        this.hallSeatsRepository = hallSeatsRepository;
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
            "19",
            UUID.randomUUID());
        performanceRepository.save(performance1);

        Performance performance2 = new Performance("라이온 킹",
            LocalDateTime.now().minusDays(5),
            LocalDateTime.now().plusDays(15),
            LocalDate.now().minusDays(3),
            LocalDate.now().plusDays(12),
            "12",
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

        // 10x10 좌석 더미 데이터 생성
        createHallSeats(hall1, performance1, 10, 10, LocalDate.now().plusDays(1));

        // 25x25 좌석 더미 데이터 생성
        createHallSeats(hall2, performance2, 25, 25, LocalDate.now().plusDays(2));
    }

    // 좌석 더미 데이터 생성 메소드
    private void createHallSeats(PerformanceHall hall, Performance performance, int rows, int columns, LocalDate performanceDate) {
        for (int row = 1; row <= rows; row++) {
            for (int column = 1; column <= columns; column++) {
                HallSeats hallSeat = HallSeats.builder()
                    .row(row)
                    .column(column)
                    .price(determinePrice(row, hall)) // 가격을 결정하는 로직 추가
                    .seatRate(determineSeatRate(row)) // 좌석 등급 결정
                    .performanceHall(hall.getName())
                    .performanceDate(performanceDate)
                    .performanceStartTime(LocalTime.of(19, 0)) // 공연 시작 시간
                    .performanceEndTime(LocalTime.of(21, 0)) // 공연 종료 시간
                    .totalSeats(rows * columns) // 전체 좌석 수
                    .build();
                // 좌석 저장
                hallSeatsRepository.save(hallSeat);
            }
        }
    }

    // 가격을 결정하는 로직 (예시)
    private int determinePrice(int row, PerformanceHall hall) {
        // 간단한 예시: 좌석 등급에 따라 가격을 다르게 설정
        if (hall.getName().equals("국립극장")) {
            if (row <= 2) {
                return 120000; // VIP
            } else if (row <= 5) {
                return 90000; // A등급
            } else {
                return 60000; // B등급
            }
        } else if (hall.getName().equals("세종문화회관")) {
            if (row <= 2) {
                return 150000; // VIP
            } else if (row <= 5) {
                return 110000; // A등급
            } else {
                return 80000; // B등급
            }
        }
        return 60000; // 기본 가격
    }

    // 좌석 등급을 결정하는 로직 (예시)
    private SeatRate determineSeatRate(int row) {
        if (row <= 2) {
            return SeatRate.S; // VIP
        } else if (row <= 5) {
            return SeatRate.A; // A등급
        } else {
            return SeatRate.B; // B등급
        }
    }

}
