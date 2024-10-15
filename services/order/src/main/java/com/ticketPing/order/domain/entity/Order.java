package com.ticketPing.order.domain.entity;


import audit.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Setter
    @Column
    private Boolean orderStatus;

    @Column
    private LocalDateTime reservationDate; // 예매 생성 시간

    @Column
    private UUID userId; // 사용자 아이디

    @Column
    private UUID scheduleId; // 공연 일정 아이디

    @Column
    private UUID companyId; // 회사명

    @Column
    private String performanceName;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Setter
    @JoinColumn(name = "order_seat_id")
    private OrderSeat orderSeat;


    @Builder
    private Order(Boolean orderStatus,
        LocalDateTime reservationDate,
        UUID userId,
        UUID scheduleId,
        String performanceName,
        UUID companyId) {
        this.orderStatus = orderStatus;
        this.reservationDate = reservationDate;
        this.userId = userId;
        this.scheduleId = scheduleId;
        this.performanceName = performanceName;
        this.companyId = companyId;
    }


}
