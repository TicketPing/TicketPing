package com.ticketPing.performance.domain.entity;

import audit.BaseEntity;
import com.ticketPing.performance.domain.entity.enums.SeatRate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "p_seat_cost")
public class SeatCost extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "seat_cost_id")
    private UUID id;

    @Enumerated(EnumType.STRING)
    private SeatRate seatRate;
    private Integer cost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_id", nullable = false)
    private Performance performance;

    public static SeatCost createTestData(SeatRate seatRate, Integer cost, Performance performance) {
        return SeatCost.builder()
                .seatRate(seatRate)
                .cost(cost)
                .performance(performance)
                .build();
    }
}


