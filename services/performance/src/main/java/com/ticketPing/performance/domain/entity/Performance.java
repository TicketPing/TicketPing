package com.ticketPing.performance.domain.entity;

import audit.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "p_performances")
public class Performance extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "performance_id")
    private UUID id;
    private String name;
    private LocalDateTime reservationStartDate;
    private LocalDateTime reservationEndDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer grade;
    private UUID companyId;

    @OneToMany(mappedBy = "performance", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SeatCost> seatCosts;

    @OneToMany(mappedBy = "performance", fetch = FetchType.LAZY)
    private List<Schedule> performanceSchedules;

    public void addSeatCost(SeatCost seatCost) {
        seatCosts.add(seatCost);
    }

    public static Performance createTestData(String name, LocalDateTime reservationStartDate, LocalDateTime reservationEndDate,
                                             LocalDate startDate, LocalDate endDate, Integer grade, UUID companyId) {
        return Performance.builder()
                .name(name)
                .reservationStartDate(reservationStartDate)
                .reservationEndDate(reservationEndDate)
                .startDate(startDate)
                .endDate(endDate)
                .grade(grade)
                .companyId(companyId)
                .seatCosts(new ArrayList<>())
                .build();
    }
}
