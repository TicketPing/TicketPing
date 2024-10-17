package com.ticketPing.payment.domain.model.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class OrderInfo {
    private UUID orderId;
    private String performanceName;
    private UUID performanceScheduleId;
    private Long amount;
    private UUID seatId;
}
