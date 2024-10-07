package com.ticketPing.order.domain.entity;


import audit.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "p_orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column
    private boolean orderCancelled; // 예매취소여부 (true or false)

    @Column
    private LocalDateTime reservationDate; // 예매 생성 시간

    @Column
    private UUID userId; // 사용자 아이디

    @Column
    private UUID scheduleId; // 공연 일정 아이디

    @Column
    private UUID companyId; // 회사명

    @Column(name = "row_number")
    private int rowNumber; // 행번호

    @Column(name = "seat_number")
    private int seatNumber; // 열번호

    @Column(name = "seat_grade")
    private String seatGrade; // 좌석등급

    @Column
    private int price; // 가격

    // 추가된 필드
    @Column
    private String performanceHall; // 공연장 이름

    @Column
    private LocalTime startTime; // 공연 시작 시간

    @Column
    private LocalTime endTime; // 공연 종료 시간

    @Column
    private LocalDate performanceDate; // 공연 날짜

    @Column
    private int totalSeats; // 전체 좌석 수

    @Setter
    @Column
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.NO_RESERVATION;

    @Builder
    private Order(
        boolean orderCancelled,
        LocalDateTime reservationDate,
        UUID userId,
        UUID scheduleId,
        UUID companyId,
        int rowNumber,
        int seatNumber,
        String seatGrade,
        int price,
        String performanceHall,
        LocalTime startTime,
        LocalTime endTime,
        LocalDate performanceDate,
        int totalSeats,
        OrderStatus orderStatus
    ) {
        this.orderCancelled = orderCancelled;
        this.reservationDate = reservationDate;
        this.userId = userId;
        this.scheduleId = scheduleId;
        this.companyId = companyId;
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        this.seatGrade = seatGrade;
        this.price = price;
        this.performanceHall = performanceHall;
        this.startTime = startTime;
        this.endTime = endTime;
        this.performanceDate = performanceDate;
        this.totalSeats = totalSeats;
        this.orderStatus = orderStatus != null ? orderStatus : OrderStatus.NO_RESERVATION; // 기본값 설정
    }
}
