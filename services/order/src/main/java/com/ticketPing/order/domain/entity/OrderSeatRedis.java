package com.ticketPing.order.domain.entity;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("order_seat")
public class OrderSeatRedis {

    @Id
    private UUID id; // Redis에서 사용되는 ID

    private boolean orderCancelled; // 예매취소여부 (true or false)
    private LocalDateTime reservationDate; // 예매 생성 시간
    private UUID userId; // 사용자 아이디
    private UUID scheduleId; // 공연 일정 아이디
    private UUID companyId; // 회사명
    private int rowNumber; // 행번호
    private int seatNumber; // 열번호
    private String seatGrade; // 좌석등급
    private int price; // 가격
    private String performanceHall; // 공연장 이름
    private LocalTime startTime; // 공연 시작 시간
    private LocalTime endTime; // 공연 종료 시간
    private LocalDate performanceDate; // 공연 날짜
    private int totalSeats; // 전체 좌석 수

    @Setter
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private OrderStatus orderStatus = OrderStatus.NO_RESERVATION; // 기본값 설정
}


