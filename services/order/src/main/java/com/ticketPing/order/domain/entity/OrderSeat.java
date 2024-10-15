package com.ticketPing.order.domain.entity;

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
@Entity
public class OrderSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "row_number")
    private int rowNumber; // 행번호

    @Column(name = "column_number")
    private int columnNumber; // 열번호

    @Column(name = "seat_grade")
    private String seatGrade; // 좌석등급

    @Column
    private int price; // 가격

    @Builder
    private OrderSeat(int rowNumber,
        int columnNumber,
        String seatGrade,
        int price
    ) {
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
        this.seatGrade = seatGrade;
        this.price = price;
    }

}
