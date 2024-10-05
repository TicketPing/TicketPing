package com.ticketPing.order.domain.entity;


import audit.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "p_orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private boolean orderCancelled; // 예매취소여부 (true or false)

    @Column(nullable = false)
    private LocalDateTime reservationDate; // 예매 생성 시간

    @Column(nullable = false)
    private UUID userId; // 사용자 아이디

    @Column(nullable = false)
    private UUID scheduleId; // 공연 일정 아이디

    @Column(nullable = false)
    private String companyName; // 회사명

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderSeat> orderSeats; // 예매좌석 리스트

    @Builder
    private Order(
        boolean orderCancelled,
        LocalDateTime reservationDate,
        UUID userId,
        UUID scheduleId,
        String companyName,
        List<OrderSeat> orderSeats
    ) {
        this.orderCancelled = orderCancelled;
        this.reservationDate = reservationDate;
        this.userId = userId;
        this.scheduleId = scheduleId;
        this.companyName = companyName;
        this.orderSeats = orderSeats;
    }
}
