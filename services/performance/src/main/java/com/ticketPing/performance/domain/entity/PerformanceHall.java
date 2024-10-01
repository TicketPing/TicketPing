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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "p_performance_hall")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerformanceHall extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private int totalSeats;

    @Column(nullable = false)
    private int rows;

    @Column(nullable = false)
    private int columns;

    @OneToMany(mappedBy = "performanceHall", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PerformanceSchedule> performanceSchedules = new ArrayList<>(); // 공연 일정 리스트

    @Builder
    public PerformanceHall(String name, String address, int totalSeats, int rows, int columns) {
        this.name = name;
        this.address = address;
        this.totalSeats = totalSeats;
        this.rows = rows;
        this.columns = columns;
    }
}


