package com.ticketPing.order.infrastructure;

import static com.ticketPing.order.presentation.response.exception.OrderExceptionCase.NOT_FOUND_ORDER_ID_IN_TTL;
import static com.ticketPing.order.presentation.response.exception.OrderExceptionCase.NOT_FOUND_SCHEDULE_ID_IN_TTL;
import static com.ticketPing.order.presentation.response.exception.OrderExceptionCase.NOT_FOUND_SEAT_ID_IN_TTL;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketPing.order.domain.entity.Order;
import com.ticketPing.order.domain.entity.RedisSeat;
import com.ticketPing.order.domain.repository.OrderRepository;
import com.ticketPing.order.domain.repository.RedisSeatRepository;
import common.exception.ApplicationException;
import java.util.UUID;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class RedisKeyExpiredListener extends KeyExpirationEventMessageListener {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisSeatRepository redisSeatRepository; // RedisSeat 저장소
    private final OrderRepository orderRepository;

    public RedisKeyExpiredListener(RedisMessageListenerContainer listenerContainer,
        RedisTemplate<String, Object> redisTemplate,
        RedisSeatRepository redisSeatRepository,
        OrderRepository orderRepository) {
        super(listenerContainer);
        this.redisTemplate = redisTemplate;
        this.redisSeatRepository = redisSeatRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString(); // 만료된 키
        System.out.println("Expired key: " + expiredKey);

        // 만료된 키에서 seatId와 orderId 추출
        String[] parts = expiredKey.split(":");
        if (parts.length == 2) { // 예: "seatId:orderId"
            String seatId = parts[0]; // seatId
            String orderId = parts[1]; // orderId

            // 만료된 키에 대한 값을 가져와 scheduleId를 확인
            String scheduleId = (String) redisTemplate.opsForValue().get(expiredKey); // 키를 통해 값 가져오기

            if (scheduleId != null) {
                // RedisSeat 객체를 가져옵니다. (scheduleId와 seatId를 사용)
                RedisSeat redisSeat = redisSeatRepository.findById(seatId)
                    .orElseThrow(() -> new ApplicationException(NOT_FOUND_SEAT_ID_IN_TTL));

                // 현재 Order 객체 가져오기
                Order order = orderRepository.findById(UUID.fromString(orderId))
                    .orElseThrow(() -> new ApplicationException(NOT_FOUND_ORDER_ID_IN_TTL));

                // 상태를 false로 설정
                order.setOrderStatus(false);
                order.setIsCancelled(true);
                orderRepository.save(order);

                // Redis에서 기존 seat 객체 가져오기
                String redisSeatJson = (String) redisTemplate.opsForValue().get("seat:" + scheduleId + ":" + seatId);

                if (redisSeatJson != null) {
                    // JSON을 RedisSeat 객체로 변환
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        redisSeat = objectMapper.readValue(redisSeatJson, RedisSeat.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                    // seatState를 false로 설정
                    redisSeat.setSeatState(false);

                    // 변경된 RedisSeat 객체를 JSON으로 직렬화하여 Redis에 저장
                    String updatedRedisSeatJson = null;
                    try {
                        updatedRedisSeatJson = objectMapper.writeValueAsString(redisSeat);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    redisTemplate.opsForValue().set("seat:" + scheduleId + ":" + seatId, updatedRedisSeatJson);
                }
            } else {
                throw new ApplicationException(NOT_FOUND_SCHEDULE_ID_IN_TTL);
            }
        }
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(this, new ChannelTopic("__keyevent@0__:expired")); // 적절한 데이터베이스 인덱스 사용
        return container;
    }
}
