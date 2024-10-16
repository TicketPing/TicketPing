//package com.ticketPing.order.application;
//
//import com.ticketPing.order.application.service.RedisOperator;
//import com.ticketPing.order.domain.entity.OrderStatus;
//import jakarta.annotation.PostConstruct;
//import java.util.HashMap;
//import java.util.Map;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import java.util.UUID;
//
//@Component
//@Slf4j
//@RequiredArgsConstructor
//public class DummyDataInitializer {
//
//    private final RedisOperator redisOperator;
//
//    @PostConstruct
//    public void init() {
//        // 더미 데이터가 이미 존재하는지 확인
//        if (!redisOperator.exists("dummy:seat:data:exists")) {
//            // 등급별 고정된 가격 설정
//            Map<String, Integer> seatRateCostMap = Map.of(
//                "S", 100000,  // S 등급 좌석 비용
//                "A", 70000,   // A 등급 좌석 비용
//                "B", 50000    // B 등급 좌석 비용
//            );
//
//            // 30개의 더미 데이터 삽입
//            for (int i = 0; i < 30; i++) {
//                UUID seatId = UUID.randomUUID();
//                int row = (int) (Math.random() * 10) + 1;  // 1~10 행
//                int col = (int) (Math.random() * 20) + 1;  // 1~20 열
//                boolean seatState = Math.random() < 0.5;   // 랜덤한 좌석 상태
//                String seatRate = i % 3 == 0 ? "S" : (i % 3 == 1 ? "A" : "B");
//                int cost = seatRateCostMap.get(seatRate);
//
//                // 좌석 정보 Map 생성
//                Map<String, String> seatData = Map.of(
//                    "row", String.valueOf(row),
//                    "col", String.valueOf(col),
//                    "seatState", String.valueOf(seatState),
//                    "seatRate", seatRate,
//                    "cost", String.valueOf(cost)
//                );
//
//                // Redis에 저장
//                redisOperator.hmset(seatId.toString(), seatData);
//            }
//
//            // 더미 데이터 존재를 나타내는 키를 설정
//            redisOperator.set("dummy:seat:data:exists", "true");
//            log.info("30개의 더미 좌석 데이터를 삽입하였습니다.");
//        } else {
//            log.info("더미 데이터가 이미 존재합니다. 삽입을 생략합니다.");
//        }
//    }
//}
