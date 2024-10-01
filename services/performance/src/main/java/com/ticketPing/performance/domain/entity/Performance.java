package com.ticketPing.performance.domain.entity;

import config.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@Table(name = "p_performance")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Performance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name; // 공연 이름

    @Column(nullable = false)
    private LocalDateTime reservationStartDate; // 예약 시작일

    @Column(nullable = false)
    private LocalDateTime reservationEndDate; // 예약 종료일

    @Column(nullable = false)
    private LocalDate startDate; // 공연 시작일

    @Column(nullable = false)
    private LocalDate endDate; // 공연 종료일

    @Column(nullable = false)
    private String grade; // 공연 등급

    @Column(nullable = false)
    private UUID companyId; // 회사 ID

    @OneToMany(mappedBy = "performance", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SeatCost> seatCosts = new ArrayList<>(); // 좌석 가격 리스트

    @OneToMany(mappedBy = "performance", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PerformanceSchedule> performanceSchedules = new ArrayList<>(); // 공연 일정 리스트

    @Builder
    public Performance(String name, LocalDateTime reservationStartDate,
        LocalDateTime reservationEndDate,
        LocalDate startDate, LocalDate endDate, String grade, UUID companyId) {
        this.name = name;
        this.reservationStartDate = reservationStartDate;
        this.reservationEndDate = reservationEndDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.grade = grade;
        this.companyId = companyId;
    }

    public Integer getCostBySeatRate(SeatRate seatRate) {
        return seatCosts.stream()
            .filter(seatCost -> seatCost.getSeatRate() == seatRate)
            .map(SeatCost::getCost) // 좌석 가격을 가져옵니다.
            .findFirst() // 첫 번째 가격을 찾습니다.
            .orElse(null); // 해당 좌석 등급이 없으면 null 반환
    }

    public List<SeatCost> seatCostList() {
        return this.seatCosts;
    }
}
