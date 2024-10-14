package com.ticketPing.order.domain.entity;


import audit.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "p_orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Setter
    @Column
    @Builder.Default
    private OrderStatus orderStatus = OrderStatus.NO_RESERVATION;

    @Column
    private LocalDateTime reservationDate; // 예매 생성 시간

    @Column
    private UUID userId; // 사용자 아이디

    @Column
    private UUID scheduleId; // 공연 일정 아이디

    @Column
    private UUID companyId; // 회사명

    public static Order from(OrderStatus orderStatus,
        LocalDateTime reservationDate,
        UUID userId,
        UUID scheduleId,
        UUID companyId) {
        return Order.builder()
            .orderStatus(orderStatus)
            .reservationDate(reservationDate)
            .userId(userId)
            .scheduleId(scheduleId)
            .companyId(companyId)
            .build();
    }

}
