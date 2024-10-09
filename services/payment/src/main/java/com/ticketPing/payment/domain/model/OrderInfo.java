package com.ticketPing.payment.domain.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class OrderInfo {
    private UUID orderId;
    private String performanceName;
    //Todo : String으로 받을 지, LocalDateTime으로 받을지
    private String performanceTime;
    private Long amount;
    private String seatInfo;
}
