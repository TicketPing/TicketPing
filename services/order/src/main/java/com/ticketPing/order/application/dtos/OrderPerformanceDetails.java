package com.ticketPing.order.application.dtos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderPerformanceDetails{

    private String performanceHallName;
    private String performanceName;
    private LocalDateTime startTime;
    private List<OrderSeatInfo> seats;

    public static OrderPerformanceDetails create(String performanceHallName, String performanceName, LocalDateTime startTime) {
        return OrderPerformanceDetails.builder()
            .performanceName(performanceName)
            .performanceHallName(performanceHallName)
            .startTime(startTime)
            .seats(new ArrayList<>())
            .build();
    }

    public void addList(OrderSeatInfo orderSeatInfo) {
        seats.add(orderSeatInfo);
    }
}
