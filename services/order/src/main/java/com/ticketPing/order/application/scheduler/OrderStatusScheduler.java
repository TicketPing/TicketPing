package com.ticketPing.order.application.scheduler;

import com.ticketPing.order.domain.entity.OrderSeatRedis;
import com.ticketPing.order.domain.entity.OrderStatus;
import com.ticketPing.order.domain.repository.OrderSeatRedisRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.StreamSupport;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusScheduler {

    private final OrderSeatRedisRepository orderSeatRedisRepository;

    public OrderStatusScheduler(OrderSeatRedisRepository orderSeatRedisRepository) {
        this.orderSeatRedisRepository = orderSeatRedisRepository;
    }

    @Scheduled(fixedRate = 60000) // 1분마다 실행 - 1초마다?
    public void updateExpiredReservations() {
        List<OrderSeatRedis> ordersToUpdate = StreamSupport.stream(
                orderSeatRedisRepository.findAll().spliterator(), false)
            .filter(orderSeatRedis ->
                orderSeatRedis.getOrderStatus().equals(OrderStatus.PENDING) &&
                    Duration.between(orderSeatRedis.getReservationDate(), LocalDateTime.now())
                        .toMinutes() >= 6)
            .toList();

        for (OrderSeatRedis orderSeatRedis : ordersToUpdate) {
            orderSeatRedis.setOrderStatus(OrderStatus.NO_RESERVATION);
            orderSeatRedisRepository.save(orderSeatRedis);
        }
    }
}
