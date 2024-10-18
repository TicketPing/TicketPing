package com.ticketPing.order.domain.model.entity;


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
import org.hibernate.annotations.Where;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "p_orders")
@Where(clause = "is_deleted = false")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Setter
    @Column
    private Boolean orderStatus;//주문 상태

    @Column
    private LocalDateTime reservationDate; // 예매 생성 시간

    @Column
    @Builder.Default
    @Setter
    private Boolean isOrderCancelled = false;//예매 취소 여부

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

    public static Order create(UUID userId, UUID companyId, String performanceName,
        LocalDateTime reservationDate, Boolean orderStatus, UUID scheduleId) {
        return Order.builder()
            .companyId(companyId)
            .performanceName(performanceName)
            .orderStatus(orderStatus)
            .userId(userId)
            .scheduleId(scheduleId)
            .reservationDate(LocalDateTime.now())
            .reservationDate(reservationDate)
            .build();
    }

}
