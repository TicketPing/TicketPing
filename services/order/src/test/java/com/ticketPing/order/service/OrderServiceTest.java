package com.ticketPing.order.service;

import static com.ticketPing.order.presentation.response.success.OrderSuccessCase.ORDER_OCCUPYING_SEAT_SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ticketPing.order.application.dtos.OrderCreateDto;
import com.ticketPing.order.application.service.OrderService;
import com.ticketPing.order.application.service.RedisOperator;
import com.ticketPing.order.client.PerformanceClient;
import com.ticketPing.order.domain.entity.OrderStatus;
import com.ticketPing.order.domain.repository.OrderRepository;
import com.ticketPing.order.domain.repository.OrderSeatRedisRepository;
import common.response.CommonResponse;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderSeatRedisRepository orderSeatRedisRepository;

    @Mock
    private PerformanceClient performanceClient;

    @Mock
    private RedissonClient redissonClient;

    @Mock
    private RedisOperator redisOperator;

    @Mock
    private RLock rLock;

    @BeforeEach
    public void setUp() {
        // RedissonClient에서 락을 반환하도록 설정
        when(redissonClient.getLock(anyString())).thenReturn(rLock);
    }

    @Test
    public void testRedLockForSeat_AcquireLockSuccessfully() throws InterruptedException {
        // 주어진 시나리오에서 락을 성공적으로 획득하는 경우
        when(rLock.tryLock(1, 10, TimeUnit.SECONDS)).thenReturn(true);
        when(redisOperator.exists(anyString())).thenReturn(true);
        when(redisOperator.get(anyString())).thenReturn(OrderStatus.NO_RESERVATION.name());

        UUID seatId = UUID.randomUUID();
        OrderCreateDto orderCreateDto = new OrderCreateDto(seatId);

        CommonResponse<Void> response = orderService.orderOccupyingSeats(orderCreateDto);

        assertEquals(ORDER_OCCUPYING_SEAT_SUCCESS.getMessage(), response.getMessage());
        verify(redisOperator).set(anyString(), eq(OrderStatus.OCCUPIED.name()));
        verify(rLock).unlock(); // 락 해제 호출 확인
    }

    @Test
    public void testRedLockForSeat_AcquireLockFail() throws ExecutionException, InterruptedException {
        UUID tempUserId = UUID.fromString("3f1b7f0a-7c8a-4b2e-bb5d-986f9c7c8b45");
        UUID seatId = UUID.randomUUID();
        UUID scheduleId = UUID.randomUUID();
        String seatIdStr = seatId.toString();
        String userSeatId = tempUserId + ":" + seatIdStr;

        // Redis 키 존재 설정
        when(redisOperator.exists(seatIdStr)).thenReturn(true);
        when(redisOperator.get(seatIdStr)).thenReturn(OrderStatus.NO_RESERVATION.name());

        // 락을 위한 Mock 설정
        RLock mockLock = mock(RLock.class);
        when(redissonClient.getLock("lock:" + seatIdStr)).thenReturn(mockLock);
        when(mockLock.tryLock(1, 10, TimeUnit.SECONDS)).thenReturn(true); // 첫 번째 스레드가 락을 성공적으로 획득

        // 첫 번째 스레드에서 orderOccupyingSeats 호출
        CompletableFuture<Void> firstThread = CompletableFuture.runAsync(() -> {
            try {
                orderService.orderOccupyingSeats(new OrderCreateDto(seatId));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // 두 번째 스레드에서 락 획득을 시도하되 실패하도록 설정
        when(mockLock.tryLock(1, 10, TimeUnit.SECONDS)).thenReturn(false);
        CompletableFuture<Void> secondThread = CompletableFuture.runAsync(() -> {
            try {
                orderService.orderOccupyingSeats(new OrderCreateDto(seatId));
            } catch (RuntimeException e) {
                System.out.println("Second thread failed to acquire lock: " + e.getMessage());
            }
        });

        // 두 스레드가 완료될 때까지 기다림
        CompletableFuture.allOf(firstThread, secondThread).get();

        // 두 번째 스레드에서 에러가 발생했는지 검증
        verify(mockLock, times(2)).tryLock(1, 10, TimeUnit.SECONDS); // 두 번 호출되었는지 확인
    }


    @Test
    public void testRedLockForSeat_ConcurrentLockAcquisition() throws InterruptedException {
        // 여러 스레드에서 동시에 락을 요청하는 경우
        when(rLock.tryLock(1, 10, TimeUnit.SECONDS)).thenReturn(true);
        when(redisOperator.exists(anyString())).thenReturn(true);
        when(redisOperator.get(anyString())).thenReturn(OrderStatus.NO_RESERVATION.name());

        ExecutorService executor = Executors.newFixedThreadPool(5);
        UUID seatId = UUID.randomUUID();

        OrderCreateDto orderCreateDto = new OrderCreateDto(seatId);

        for (int i = 0; i < 5; i++) {
            executor.submit(() -> {
                orderService.orderOccupyingSeats(orderCreateDto);
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        // 성공적으로 OCCUPIED 상태로 변경된 수를 검증
        verify(redisOperator, atLeastOnce()).set(anyString(), eq(OrderStatus.OCCUPIED.name()));
    }
}
