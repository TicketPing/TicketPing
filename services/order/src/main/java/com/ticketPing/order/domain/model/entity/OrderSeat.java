package com.ticketPing.order.domain.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_seats")
@Builder(access = AccessLevel.PRIVATE)
@Entity
public class OrderSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column
    private UUID seatId;

    @Column(name = "row_number")
    private int row; // 행번호

    @Column(name = "column_number")
    private int col; // 열번호

    @Column(name = "seat_grade")
    private String seatGrade; // 좌석등급

    @Column
    private int cost; // 가격

    public static OrderSeat create(UUID seatId, int row, int col, String seatGrade, int cost) {
        return OrderSeat.builder()
            .seatId(seatId)
            .col(col)
            .seatGrade(seatGrade)
            .cost(cost)
            .row(row)
            .build();
    }

}
