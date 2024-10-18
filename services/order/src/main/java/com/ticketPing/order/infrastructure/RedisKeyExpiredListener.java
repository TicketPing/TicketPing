package com.ticketPing.order.infrastructure;

import static com.ticketPing.order.presentation.cases.exception.OrderExceptionCase.JSON_PROCESSING_EXCEPTION;
import static com.ticketPing.order.presentation.cases.exception.OrderExceptionCase.NOT_FOUND_ORDER_ID_IN_TTL;
import static com.ticketPing.order.presentation.cases.exception.OrderExceptionCase.NOT_FOUND_SCHEDULE_ID_IN_TTL;
import static com.ticketPing.order.presentation.cases.exception.OrderExceptionCase.ORDER_FOR_PERFORMANCE_CACHE_NOT_FOUND;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketPing.order.domain.model.entity.Order;
import com.ticketPing.order.domain.model.entity.RedisSeat;
import com.ticketPing.order.domain.repository.OrderRepository;
import com.ticketPing.order.infrastructure.service.RedisService;
import common.exception.ApplicationException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisKeyExpiredListener implements MessageListener {

    private final RedisService redisService;
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString(); // 만료된 키
        System.out.println("Expired key: " + expiredKey);

        // 만료된 키에서 seatId와 orderId 추출
        String[] parts = expiredKey.split(":");
        if (parts.length == 3) {
            String scheduleId = parts[0];
            String seatId = parts[1]; // seatId
            String orderId = parts[2]; // orderId

            // 현재 Order 객체 가져오기
            Order order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_ORDER_ID_IN_TTL));

            // 상태를 false로 설정
            order.setOrderStatus(false);
            orderRepository.save(order);

            updateRedisSeatState(scheduleId, seatId);

        } else {
            throw new ApplicationException(NOT_FOUND_SCHEDULE_ID_IN_TTL);
        }

    }

    private void updateRedisSeatState(String scheduleId, String seatId) {
        RedisSeat redisSeat;
        String key = "seat:" + scheduleId + ":" + seatId;
        String redisSeatJson = redisService.getValue(key);

        if (redisSeatJson != null) {

            redisSeat = getRedisSeat(key);

            // seatState를 false로 설정
            redisSeat.setSeatState(false);

            // 변경된 RedisSeat 객체를 JSON으로 직렬화하여 Redis에 저장

            String updatedRedisSeatJson = getRedisWriteSeat(redisSeat);

            redisService.setValue("seat:" + scheduleId + ":" + seatId, updatedRedisSeatJson);
        }
    }

    private RedisSeat getRedisSeat(String redisKey) {
        String value = redisService.getValue(redisKey);
        if (value == null) {
            throw new ApplicationException(ORDER_FOR_PERFORMANCE_CACHE_NOT_FOUND);
        }

        try {
            return objectMapper.readValue(value, RedisSeat.class);
        } catch (JsonProcessingException e) {
            log.error("JSON 역직렬화 오류: {}", e.getMessage());
            throw new ApplicationException(JSON_PROCESSING_EXCEPTION);
        }
    }

    private String getRedisWriteSeat(RedisSeat redisSeat) {
        if (redisSeat == null) {
            throw new ApplicationException(ORDER_FOR_PERFORMANCE_CACHE_NOT_FOUND);
        }

        try {
            return objectMapper.writeValueAsString(redisSeat);
        } catch (JsonProcessingException e) {
            log.error("JSON 역직렬화 오류: {}", e.getMessage());
            throw new ApplicationException(JSON_PROCESSING_EXCEPTION);
        }
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(this,
            new ChannelTopic("__keyevent@0__:expired")); // 적절한 데이터베이스 인덱스 사용
        return container;
    }
}
