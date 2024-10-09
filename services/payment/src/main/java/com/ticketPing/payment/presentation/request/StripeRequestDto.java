package com.ticketPing.payment.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StripeRequestDto {

    private String performanceName;
    private String performanceTime;
    private String seatInfo;
    private Long amount;
    private String userEmail;


}
