package dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderPerformanceDto {
    private int seatRow; // 행
    private int seatColumn; // 열
    private int price; // 가격
    private String seatRate; // 좌석 등급 (String으로 변경)
    private String performanceHall; // 공연장 이름
    private LocalTime startTime; // 공연 시작 시간
    private LocalTime endTime; // 공연 종료 시간
    private LocalDate performanceDate; // 공연 날짜
    private int totalSeats; // 전체 좌석 수
    private UUID performanceHallId; // 공연장 ID
    private boolean orderCancelled; // 예매취소여부
    private LocalDateTime reservationDate; // 예매 생성 시간
    private UUID userId; // 사용자 아이디
    private UUID scheduleId; // 공연 일정 아이디
    private UUID companyId; // 회사명
}

