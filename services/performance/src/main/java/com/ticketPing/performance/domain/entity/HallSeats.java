package com.ticketPing.performance.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "p_hall_seats") // 테이블 이름을 hall_seats로 설정
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자
@AllArgsConstructor // 모든 필드를 포함하는 생성자
public class HallSeats {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // 자동 증가 ID
    @Column(nullable = false) // ID 필드
    private UUID id;

    @Column(nullable = false) // 행
    private int seatRow;

    @Column(nullable = false) // 열
    private int seatColumn;

    @Column(nullable = false) // 가격
    private int price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SeatRate seatRate;

    @Column(nullable = false) // 공연장 이름
    private String performanceHall;

    @Column(nullable = false)
    private LocalTime startTime; // 공연 시작 시간

    @Column(nullable = false)
    private LocalTime endTime; // 공연 종료 시간

    @Column(nullable = false) // 공연 날짜
    private LocalDate performanceDate; // LocalDate 사용 가능

    @Column(nullable = false)
    private UUID performanceHallId;

    @Column(nullable = false) // 전체 좌석 수
    private int totalSeats; // 전체 좌석 수

    @Builder
    public HallSeats(int seatRow, int seatColumn, int price, SeatRate seatRate, String performanceHall,
        LocalTime startTime, LocalTime endTime, LocalDate performanceDate,
        int totalSeats, UUID performanceHallId) {
        this.seatRow = seatRow;
        this.seatColumn = seatColumn;
        this.price = price;
        this.seatRate = seatRate;
        this.performanceHall = performanceHall;
        this.startTime = startTime;
        this.endTime = endTime;
        this.performanceDate = performanceDate;
        this.totalSeats = totalSeats;
        this.performanceHallId = performanceHallId;
    }

}

