package com.ticketPing.order.application.service;

import static com.ticketPing.order.presentation.response.exception.OrderExceptionCase.ORDER_ALREADY_OCCUPIED;
import static com.ticketPing.order.presentation.response.exception.OrderExceptionCase.ORDER_FOR_PERFORMANCE_CACHE_NOT_FOUND;
import static com.ticketPing.order.presentation.response.exception.OrderExceptionCase.ORDER_NOT_FOUND;
import static com.ticketPing.order.presentation.response.exception.OrderExceptionCase.ORDER_NOT_FOUND_AT_REDIS;
import static com.ticketPing.order.presentation.response.exception.OrderExceptionCase.ORDER_STATUS_UNKNOWN;
import static com.ticketPing.order.presentation.response.exception.OrderExceptionCase.TTL_ALREADY_EXISTS;
import static com.ticketPing.order.presentation.response.success.OrderSuccessCase.ORDER_OCCUPYING_SEAT_SUCCESS;

import com.ticketPing.order.application.dtos.OrderCreateDto;
import com.ticketPing.order.application.dtos.OrderResponse;
import com.ticketPing.order.application.dtos.temp.OrderInfoResponse;
import com.ticketPing.order.application.dtos.temp.SeatResponse;
import com.ticketPing.order.client.PaymentClient;
import com.ticketPing.order.client.PerformanceClient;
import com.ticketPing.order.client.UserClient;
import com.ticketPing.order.domain.entity.Order;
import com.ticketPing.order.domain.entity.OrderSeat;
import com.ticketPing.order.domain.entity.OrderStatus;
import com.ticketPing.order.domain.entity.RedisSeat;
import com.ticketPing.order.domain.events.OrderCompletedEvent;
import com.ticketPing.order.domain.repository.OrderRepository;
import com.ticketPing.order.domain.repository.OrderSeatRepository;
import com.ticketPing.order.domain.repository.RedisSeatRepository;
import com.ticketPing.order.presentation.response.exception.OrderExceptionCase;
import common.exception.ApplicationException;
import common.response.CommonResponse;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository; // RDB에 대한 리포지토리
    private final RedisSeatRepository orderSeatRedisRepository; // Redis 리포지토리
    private final OrderSeatRepository orderSeatRepository;
    private final PerformanceClient performanceClient;
    private final PaymentClient paymentClient;
    private final UserClient userClient;
    private final RedissonClient redissonClient;
    private final RedisOperator redisOperator;
    private final EventApplicationService eventApplicationService;

    private final static int SEAT_LOCK_CACHE_EXPIRE_SECONDS = 330;
    private final RedisSeatRepository redisSeatRepository;

    UUID tempUserId = UUID.fromString(
        "3f1b7f0a-7c8a-4b2e-bb5d-986f9c7c8b45"); // TODO : 실제 userId 값으로 변경

    @Transactional
    public CommonResponse<OrderResponse> orderOccupyingSeats(OrderCreateDto orderCreateRequestDto) {

        Optional<RedisSeat> redisSeat = redisSeatRepository.findBySeatId(
            String.valueOf(orderCreateRequestDto.seatId()));

        String seatId = orderCreateRequestDto.seatId().toString();
        String userSeatId = tempUserId + ":" + seatId;

        if (!redisOperator.exists(seatId)) {
            return CommonResponse.error(ORDER_FOR_PERFORMANCE_CACHE_NOT_FOUND);
        }

        // Redis에서 값을 가져와서 OrderStatus로 변환
        OrderStatus currentStatus = getOrderStatusFromRedis(seatId);
        if (currentStatus == OrderStatus.OCCUPIED) {
            return CommonResponse.error(ORDER_ALREADY_OCCUPIED);
        }

        if (redisSeat.isPresent()) {
            return CommonResponse.error(ORDER_ALREADY_OCCUPIED);
        }

        // 분산 락 설정
        return redLockForSeat(String.valueOf(tempUserId), seatId, redisSeat.get());
    }

    public CommonResponse<OrderResponse> redLockForSeat(String userId, String seatId,
        RedisSeat redisSeat) {
        String lockKey = "lock:" + seatId;
        RLock lock = redissonClient.getLock(lockKey);
        try {
            // 락 시도
            if (lock.tryLock(1, 10, TimeUnit.SECONDS)) { // 1초 대기 후 10초 동안 락 유지
                try {

                    Order order = orderWithOrderSeatSave(
                        UUID.fromString(seatId));

                    String SeatIdWithTTL = seatId + ":" + order.getId();//orderId:seatId

                    if (redisOperator.exists(SeatIdWithTTL)) {
                        throw new ApplicationException(TTL_ALREADY_EXISTS);//exceptionCase
                    }
                    redisOperator.setIfAbsentWithTTL(SeatIdWithTTL, userId,
                        SEAT_LOCK_CACHE_EXPIRE_SECONDS);

                    redisSeat.setSeatState(true);
                    redisSeatRepository.save(redisSeat);

                    OrderResponse orderResponse = OrderResponse.builder()
                        .id(order.getId())
                        .rowNumber(order.getOrderSeat().getRowNumber())
                        .columnNumber(order.getOrderSeat().getColumnNumber())
                        .price(order.getOrderSeat().getPrice())
                        .userId(order.getUserId())
                        .performanceName(order.getPerformanceName())
                        .companyId(order.getCompanyId())
                        .orderStatus(order.getOrderStatus())
                        .reservationDate(order.getReservationDate())
                        .scheduleId(order.getScheduleId())
                        .seatGrade(order.getOrderSeat().getSeatGrade())
                        .build();

                    return CommonResponse.success(ORDER_OCCUPYING_SEAT_SUCCESS, orderResponse);

                } finally {
                    lock.unlock(); // 락 해제
                }
            } else {
                throw new RuntimeException(ORDER_ALREADY_OCCUPIED.getMessage());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 스레드 인터럽트 상태 복원
            throw new RuntimeException(
                OrderExceptionCase.LOCK_ACQUISITION_INTERRUPTED.getMessage());
        }
    }

    private OrderStatus getOrderStatusFromRedis(String key) {
        String value = redisOperator.get(key);
        if (value == null) {
            return OrderStatus.NO_RESERVATION;
        }
        try {
            return OrderStatus.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(ORDER_STATUS_UNKNOWN.getMessage());
        }
    }

//    @Transactional
//    public void orderRequestPaymentForOccupiedSeats(OrderCreateDto orderCreateRequestDto) {
//        UUID seatId = orderCreateRequestDto.seatId();
//
//        RedisSeat orderSeatRedis = orderSeatRedisRepository.findById(
//                seatId)
//            .orElseThrow(() -> new RuntimeException(OrderExceptionCase.ORDER_NOT_FOUND.getMessage()));
//
//        // 좌석 예약 상태 확인
//        if (!orderSeatRedis.getSeatState()) {
//            throw new RuntimeException(ORDER_SEATS_NOT_OCCUPIED.getMessage());
//        }
//
//        OrderInfoResponse orderInfoResponse = findOrder(seatId);
//
//        //TODO : orderId 추가하기
//        StripeRequestDto stripeRequestDto = StripeRequestDto.builder()
//            .amount(Long.valueOf(orderInfoResponse.cost()))
//            .seatInfo(orderInfoResponse.seatRate())
//            .performanceTime(String.valueOf(orderInfoResponse.startTime()))
//            .performanceName(orderInfoResponse.performanceName())
//            .userEmail("tempUser@gmail.com")
//            .build();
//
//        orderSeatRedis.setSeatState(true);
//        redisSeatRepository.save(orderSeatRedis);
//
//
//        // TODO : ResponseEntity<CommonResponse<UserResponse>> getUser = userClient.getUser(tempUserId); 이메일 받기
//
//        StripeResponseDto stripeResponseDto = paymentClient.createPaymentIntent(stripeRequestDto);
//
//    }

    public Order orderWithOrderSeatSave(UUID seatId) {
        ResponseEntity<CommonResponse<OrderInfoResponse>> orderInfo = performanceClient.getOrderInfo(
            seatId);
        // 주문 데이터를 생성하고 저장
        Order order = Order.builder()
            .companyId(orderInfo.getBody().getData().companyId())
            .performanceName(orderInfo.getBody().getData().performanceName())
            .orderStatus(orderInfo.getBody().getData().seatState())
            .userId(tempUserId)
            .scheduleId(orderInfo.getBody().getData().scheduleId())
            .reservationDate(LocalDateTime.now())
            .build();

        // 좌석 정보 설정
        OrderSeat orderSeat = OrderSeat.builder()
            .columnNumber(orderInfo.getBody().getData().col())
            .seatGrade(orderInfo.getBody().getData().seatRate())
            .price(orderInfo.getBody().getData().cost())
            .rowNumber(orderInfo.getBody().getData().row())
            .build();

        order.setOrderSeat(orderSeat);
        orderSeatRepository.save(orderSeat);
        orderRepository.save(order);

        return order;
    }

    public OrderInfoResponse findOrder(UUID seatId) {
        ResponseEntity<CommonResponse<OrderInfoResponse>> orderInfo = performanceClient.getOrderInfo(
            seatId);
        return orderInfo.getBody().getData();
    }

    @Transactional
    public void updateOrderStatus(UUID orderId, String status) {
//        if(status.equals("fail")) {
//            throw new ApplicationException();//상태
//        }

        RedisSeat orderSeatRedis = orderSeatRedisRepository.findById(
                orderId)
            .orElseThrow(() -> new RuntimeException(ORDER_NOT_FOUND_AT_REDIS.getMessage()));

        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException(ORDER_NOT_FOUND.getMessage()));

        // TODO : order 상태 변경

        order.setOrderStatus(false);
        orderRepository.save(order);

        // TODO : order redis 상태 변경

        orderSeatRedis.setSeatState(false);
        redisSeatRepository.save(orderSeatRedis);

        // TODO : performance API 호출

        ResponseEntity<CommonResponse<SeatResponse>> savedPerformanceToDb
            = performanceClient.updateSeatState(orderSeatRedis.getSeatId(),
            orderSeatRedis.getSeatState());

        String performanceId = "1";
        eventApplicationService.publishOrderCompletedEvent(
            OrderCompletedEvent.create(String.valueOf(tempUserId), performanceId));

    }

    public void test() {
        // 예매 완료 이벤트 발행
        String userId = "1";
        String performanceId = "1";
        eventApplicationService.publishOrderCompletedEvent(
            OrderCompletedEvent.create(userId, performanceId));
    }

}
