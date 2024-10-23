package com.ticketPing.order.domain.model.entity;

import audit.BaseEntity;
import jakarta.persistence.*;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "p_order_seats")
@Where(clause = "is_deleted = false")
@Entity
public class OrderSeat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_seat_id")
    private UUID id;
    private UUID seatId;
    private int row;
    private int col;
    private String seatRate;
    private int cost;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    public static OrderSeat create(UUID seatId, int row, int col, String seatRate, int cost) {
        return OrderSeat.builder()
            .seatId(seatId)
            .col(col)
            .seatRate(seatRate)
            .cost(cost)
            .row(row)
            .build();
    }

}
