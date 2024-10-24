package com.ticketPing.order.infrastructure;

import com.ticketPing.order.application.dtos.temp.SeatResponse;
import com.ticketPing.order.domain.model.entity.Order;
import com.ticketPing.order.domain.model.enums.OrderStatus;
import com.ticketPing.order.infrastructure.repository.OrderRepository;
import com.ticketPing.order.infrastructure.service.RedisService;
import common.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.ticketPing.order.presentation.cases.exception.OrderExceptionCase.NOT_FOUND_ORDER_ID_IN_TTL;
import static com.ticketPing.order.presentation.cases.exception.OrderExceptionCase.NOT_FOUND_SCHEDULE_ID_IN_TTL;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisKeyExpiredListener implements MessageListener {

    private final RedisService redisService;
    private final OrderRepository orderRepository;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString(); // 만료된 키
        log.info("Expired key: " + expiredKey);

        // 만료된 키에서 seatId와 orderId 추출
        String[] parts = expiredKey.split(":");
        if (parts.length == 4) {
            String scheduleId = parts[1];
            String seatId = parts[2]; // seatId
            String orderId = parts[3]; // orderId

            // 현재 Order 객체 가져오기
            Order order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_ORDER_ID_IN_TTL));

            // ttl 만료되면 상태 변경하는 로직
            order.updateOrderStatus(OrderStatus.RESERVATION_FAIL);
            orderRepository.save(order);

            updateRedisSeatState(scheduleId, seatId);

        } else {
            throw new ApplicationException(NOT_FOUND_SCHEDULE_ID_IN_TTL);
        }

    }

    private void updateRedisSeatState(String scheduleId, String seatId) {
        String key = "seat:" + scheduleId + ":" + seatId;
        SeatResponse seatResponse = redisService.getValueAsClass(key, SeatResponse.class);
        seatResponse.updateSeatState(false);
        redisService.setValue(key, seatResponse);
    }
}
