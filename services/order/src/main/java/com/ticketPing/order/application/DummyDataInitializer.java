package com.ticketPing.order.application;

import com.ticketPing.order.application.service.RedisOperator;
import com.ticketPing.order.domain.entity.OrderStatus;
import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class DummyDataInitializer {

    private final RedisOperator redisOperator;

    @PostConstruct
    public void init() {
        // 더미 데이터가 이미 존재하는지 확인
        if (!redisOperator.exists("dummy:data:exists")) {
            // 30개의 더미 데이터 삽입
            for (int i = 0; i < 30; i++) {
                String seatId = String.valueOf(UUID.randomUUID()); // 랜덤 UUID 생성
                UUID scheduleId = UUID.randomUUID(); // 랜덤 UUID 생성

                // 초기 상태를 NO_RESERVATION으로 설정
                redisOperator.set(seatId, OrderStatus.NO_RESERVATION.name());
                // 더미 데이터 존재를 나타내는 키를 설정
                redisOperator.set("dummy:data:exists", "true"); // 데이터가 존재하는 경우를 기록

                // 로그를 추가하여 삽입된 데이터를 확인할 수 있습니다.
                System.out.println("Inserted dummy data with key: " + seatId + " and value: " + OrderStatus.NO_RESERVATION.name());
            }
        } else {
            // 이미 더미 데이터가 존재하는 경우 로그 출력
            System.out.println("Dummy data already exists. Skipping insertion.");
        }
    }
}