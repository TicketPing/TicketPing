package com.ticketPing.order.domain.model.entity;


import audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "p_orders")
@Where(clause = "is_deleted = false")
@Entity
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id")
    private UUID id;
    private Boolean orderStatus;
    private Boolean isOrderCancelled;
    private LocalDateTime reservationDate;
    private UUID userId;
    private UUID scheduleId;
    private UUID companyId;
    private String performanceName;

    @Setter
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_seat_id")
    private OrderSeat orderSeat;

    public static Order create(UUID userId, UUID companyId, String performanceName,
        LocalDateTime reservationDate, Boolean orderStatus, UUID scheduleId) {
        return Order.builder()
                .companyId(companyId)
                .performanceName(performanceName)
                .orderStatus(orderStatus)
                .isOrderCancelled(false)
                .userId(userId)
                .scheduleId(scheduleId)
                .reservationDate(LocalDateTime.now())
                .reservationDate(reservationDate)
                .build();
    }

    public void updateOrderStatus(Boolean orderStatus) {
        this.orderStatus = orderStatus;
    }
}
