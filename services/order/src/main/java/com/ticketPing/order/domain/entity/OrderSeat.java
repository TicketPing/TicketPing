//package com.ticketPing.order.domain.entity;
//
//import com.ticketPing.order.application.dtos.HallSeatsDto;
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.EnumType;
//import jakarta.persistence.Enumerated;
//import jakarta.persistence.FetchType;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.Table;
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.UUID;
//import lombok.AccessLevel;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//
//@Getter
//@Entity
//@Table(name = "p_seats")
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class OrderSeat {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private UUID id;
//
//    @Column(name = "row_number")
//    private int rowNumber; // 행번호
//
//    @Column(name = "seat_number")
//    private int seatNumber; // 열번호
//
//    @Column(name = "seat_grade")
//    private String seatGrade; // 좌석등급
//
//    @Column
//    private int price; // 가격
//
//    // 추가된 필드
//    @Column
//    private String performanceHall; // 공연장 이름
//
//    @Column
//    private LocalTime startTime; // 공연 시작 시간
//
//    @Column
//    private LocalTime endTime; // 공연 종료 시간
//
//    @Column
//    private LocalDate performanceDate; // 공연 날짜
//
//    @Column
//    private int totalSeats; // 전체 좌석 수
//
//    @Setter
//    @Column
//    @Enumerated(EnumType.STRING)
//    private OrderStatus orderStatus = OrderStatus.NO_RESERVATION; // 예매 상태 (예: "점유됨-RESERVATION", "결제 진행중-NO_RESERVATION", "점유되지 않음-PENDING")
//
//    @Builder
//    private OrderSeat(int rowNumber,
//        int seatNumber,
//        String seatGrade,
//        int price,
//        Order order,
//        String performanceHall,
//        LocalTime startTime,
//        LocalTime endTime,
//        LocalDate performanceDate,
//        int totalSeats) {
//        this.rowNumber = rowNumber;
//        this.seatNumber = seatNumber;
//        this.seatGrade = seatGrade;
//        this.price = price;
//        this.order = order;
//        this.performanceHall = performanceHall;
//        this.startTime = startTime;
//        this.endTime = endTime;
//        this.performanceDate = performanceDate;
//        this.totalSeats = totalSeats;
//    }
//
//    // HallSeatsDto로부터 OrderSeat 객체를 생성하는 정적 메서드 추가
//    public static OrderSeat fromHallSeatsDto(HallSeatsDto hallSeatsDto, Order order) {
//        return OrderSeat.builder()
//            .rowNumber(hallSeatsDto.getSeatRow())
//            .seatNumber(hallSeatsDto.getSeatColumn())
//            .seatGrade(hallSeatsDto.getSeatRate())
//            .price(hallSeatsDto.getPrice())
//            .order(order)
//            .performanceHall(hallSeatsDto.getPerformanceHall())
//            .startTime(hallSeatsDto.getStartTime())
//            .endTime(hallSeatsDto.getEndTime())
//            .performanceDate(hallSeatsDto.getPerformanceDate())
//            .totalSeats(hallSeatsDto.getTotalSeats())
//            .build();
//    }
//}
