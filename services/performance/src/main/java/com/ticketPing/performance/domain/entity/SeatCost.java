package com.ticketPing.performance.domain.entity;

import config.BaseEntity;
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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "p_seat_cost")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeatCost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING) // Enum을 문자열로 저장
    private SeatRate seatRate; // 좌석 등급

    @Column(nullable = false)
    private Integer cost; // 좌석 가격

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_id", nullable = false)
    private Performance performance; // 공연 참조

    @Builder
    public SeatCost(SeatRate seatRate, Integer cost, Performance performance) {
        this.seatRate = seatRate;
        this.cost = cost;
        this.performance = performance;
    }
}


