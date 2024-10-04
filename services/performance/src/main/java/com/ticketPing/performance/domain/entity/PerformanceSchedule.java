package com.ticketPing.performance.domain.entity;

import audit.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "p_performance_schedule")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerformanceSchedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private LocalTime startTime; // 공연 시작 시간

    @Column(nullable = false)
    private LocalTime endTime; // 공연 종료 시간

    @Column(nullable = false)
    private LocalDate scheduledDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_hall_id", nullable = false)
    private PerformanceHall performanceHall; // 공연장 참조

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_id", nullable = false)
    private Performance performance; // 공연 참조

    @Builder
    public PerformanceSchedule(LocalTime startTime, LocalTime endTime,
        PerformanceHall performanceHall, Performance performance, LocalDate scheduledDate) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.performanceHall = performanceHall;
        this.performance = performance;
        this.scheduledDate = scheduledDate;
    }
}
