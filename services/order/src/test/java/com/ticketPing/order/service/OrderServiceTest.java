package com.ticketPing.order.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ticketPing.order.application.dtos.OrderCreateRequestDto;
import com.ticketPing.order.application.service.OrderService;
import com.ticketPing.order.domain.entity.OrderSeatRedis;
import com.ticketPing.order.domain.entity.OrderStatus;
import com.ticketPing.order.domain.repository.OrderSeatRedisRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderSeatRedisRepository orderSeatRedisRepository;

    @Mock
    private RedissonClient redissonClient;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void testOrderPerformanceSeatsWithDistributedLock() throws InterruptedException {
        // Given
        UUID orderId = UUID.randomUUID();
        OrderSeatRedis orderSeatRedis = new OrderSeatRedis();
        orderSeatRedis.setOrderStatus(OrderStatus.NO_RESERVATION);

        when(orderSeatRedisRepository.findById(orderId)).thenReturn(
            java.util.Optional.of(orderSeatRedis));

        RLock mockLock = mock(RLock.class);
        when(redissonClient.getLock("orderLock:" + orderId)).thenReturn(mockLock);
        when(mockLock.tryLock(1, 10, TimeUnit.SECONDS)).thenReturn(true).thenReturn(false);

        // 여러 스레드를 사용하여 동시 접근 테스트
        ExecutorService executor = Executors.newFixedThreadPool(50);
        Runnable task = () -> {
            try {
                OrderCreateRequestDto requestDto = new OrderCreateRequestDto(
                    "Hall A",
                    "Performance 1",
                    LocalTime.now(),
                    LocalTime.now().plusHours(2),
                    orderId,
                    LocalDate.now()
                );

                orderService.orderPerformanceSeats(requestDto);
            } catch (Exception e) {
                // 예외 처리
                e.printStackTrace();
            }
        };

        // When: 두 스레드가 동시에 실행
        executor.submit(task);
        executor.submit(task);

        // Allow some time for tasks to complete
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // Then: Verify that the lock was used and released
        verify(mockLock, times(2)).tryLock(1, 10, TimeUnit.SECONDS);
        verify(mockLock, times(1)).unlock();
    }

}

