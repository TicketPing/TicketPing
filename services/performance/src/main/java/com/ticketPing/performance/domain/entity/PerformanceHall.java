package com.ticketPing.performance.domain.entity;

import audit.BaseEntity;
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

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "p_performance_halls")
public class PerformanceHall extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "performance_hall_name")
    private UUID id;
    private String name;
    private String address;
    private Integer seatNumber;
    private Integer rows;
    private Integer columns;

    @OneToMany(mappedBy = "performanceHall", fetch = FetchType.LAZY)
    private List<Schedule> performanceSchedules;

    public static PerformanceHall createTestData(String name, String address, Integer seatNumber,
                                                 Integer rows, Integer columns) {
        return PerformanceHall.builder()
                .name(name)
                .address(address)
                .seatNumber(seatNumber)
                .rows(rows)
                .columns(columns)
                .build();
    }
}


