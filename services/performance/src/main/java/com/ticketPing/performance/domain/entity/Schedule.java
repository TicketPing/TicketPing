package com.ticketPing.performance.domain.entity;

import audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "p_schedules")
public class Schedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "schedule_id")
    private UUID id;
    private LocalDateTime startTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_hall_id")
    private PerformanceHall performanceHall;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_id")
    private Performance performance;

    @OneToMany(mappedBy = "schedule", fetch = FetchType.LAZY)
    private List<Seat> seats;

    public static Schedule createTestData(LocalDateTime startTime, PerformanceHall performanceHall, Performance performance) {
        return Schedule.builder()
                .startTime(startTime)
                .performance(performance)
                .performanceHall(performanceHall)
                .seats(new ArrayList<>())
                .build();
    }
}
