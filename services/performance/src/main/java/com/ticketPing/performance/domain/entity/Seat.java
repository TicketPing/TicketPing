package com.ticketPing.performance.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "p_seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "seat_id")
    private UUID id;
    private Integer row;
    private Integer col;
    private Boolean seatState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_cost_id")
    private SeatCost seatCost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    public static Seat createTestData(Integer row, Integer col, Boolean seatSate, SeatCost seatCosts, Schedule schedule) {
        return Seat.builder()
                .row(row)
                .col(col)
                .seatState(seatSate)
                .seatCost(seatCosts)
                .schedule(schedule)
                .build();
    }

    public void updateSeatState(Boolean seatState) {
        this.seatState = seatState;
    }
}
