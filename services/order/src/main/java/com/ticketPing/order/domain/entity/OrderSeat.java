package com.ticketPing.order.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "p_seats")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "row_number", nullable = false)
    private int rowNumber; // 행번호

    @Column(name = "seat_number", nullable = false)
    private int seatNumber; // 열번호

    @Column(name = "seat_grade", nullable = false)
    private String seatGrade; // 좌석등급

    @Column(nullable = false)
    private int price; // 가격

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false) // 외래 키 컬럼 이름 지정
    private Order order; // 예매ID (Order 객체로 참조)

    @Builder
    private OrderSeat( int rowNumber,
        int seatNumber,
        String seatGrade,
        int price,
        Order order
    ) {
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        this.seatGrade = seatGrade;
        this.price = price;
        this.order = order;
    }
}
