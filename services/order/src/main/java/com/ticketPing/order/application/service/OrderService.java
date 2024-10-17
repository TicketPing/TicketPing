package com.ticketPing.order.application.service;

import static com.ticketPing.order.presentation.cases.exception.OrderExceptionCase.JSON_PROCESSING_EXCEPTION;
import static com.ticketPing.order.presentation.cases.exception.OrderExceptionCase.LOCK_ACQUISITION_FAIL;
import static com.ticketPing.order.presentation.cases.exception.OrderExceptionCase.ORDER_ALREADY_OCCUPIED;
import static com.ticketPing.order.presentation.cases.exception.OrderExceptionCase.ORDER_FOR_PERFORMANCE_CACHE_NOT_FOUND;
import static com.ticketPing.order.presentation.cases.exception.OrderExceptionCase.ORDER_IN_REDIS_NOT_OCCUPIED;
import static com.ticketPing.order.presentation.cases.exception.OrderExceptionCase.ORDER_NOT_FOUND;
import static com.ticketPing.order.presentation.cases.exception.OrderExceptionCase.ORDER_NOT_FOUND_AT_REDIS;
import static com.ticketPing.order.presentation.cases.exception.OrderExceptionCase.REQUEST_ORDER_INFORMATION_BY_PAYMENT_NOT_FOUND;
import static com.ticketPing.order.presentation.cases.exception.OrderExceptionCase.THE_SEAT_ALREADY_PAID_BY_SOMEONE;
import static com.ticketPing.order.presentation.cases.exception.OrderExceptionCase.TTL_ALREADY_EXISTS;
import static com.ticketPing.order.presentation.cases.exception.OrderExceptionCase.TTL_NOT_FOUND;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketPing.order.application.dtos.OrderCreateDto;
import com.ticketPing.order.application.dtos.OrderResponse;
import com.ticketPing.order.application.dtos.OrderInfoResponse;
import com.ticketPing.order.application.dtos.temp.SeatResponse;
import com.ticketPing.order.client.PerformanceClient;
import com.ticketPing.order.domain.model.entity.Order;
import com.ticketPing.order.domain.model.entity.OrderSeat;
import com.ticketPing.order.domain.model.entity.RedisSeat;
import com.ticketPing.order.domain.events.OrderCompletedEvent;
import com.ticketPing.order.domain.repository.OrderRepository;
import com.ticketPing.order.domain.repository.OrderSeatRepository;
import com.ticketPing.order.domain.repository.RedisSeatRepository;
import com.ticketPing.order.infrastructure.service.RedisService;
import common.exception.ApplicationException;
import common.response.CommonResponse;
import dto.PaymentRequestDto;
import dto.PaymentResponseDto;
import java.time.LocalDateTime;
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
    private final OrderSeatRepository orderSeatRepository;
    private final PerformanceClient performanceClient;
    private final EventApplicationService eventApplicationService;
    private final RedisService redisService;
    private final ObjectMapper objectMapper;
    private final RedissonClient redissonClient;

    private final static int SEAT_LOCK_CACHE_EXPIRE_SECONDS = 330;
    private final RedisSeatRepository redisSeatRepository;

    @Transactional
    public OrderResponse orderOccupyingSeats(OrderCreateDto orderCreateRequestDto, UUID userId) {

        String scheduleId = orderCreateRequestDto.scheduleId().toString();
        String seatId = orderCreateRequestDto.seatId().toString();
        String redisKey = "seat:" + scheduleId + ":" + seatId;

        if (Boolean.FALSE.equals(redisService.hasKey(redisKey))) {
            throw new ApplicationException(ORDER_FOR_PERFORMANCE_CACHE_NOT_FOUND);
        }

        RedisSeat redisSeat = getRedisSeat(redisKey);

        if(redisSeat.getSeatState()) {
            throw new ApplicationException(ORDER_ALREADY_OCCUPIED);
        }

        return redLockForSeat(userId, seatId, redisSeat, scheduleId);
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


    private OrderResponse redLockForSeat(UUID userId, String seatId, RedisSeat redisSeat, String scheduleId) {

        String lockKey = "lock:" + seatId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            if (lock.tryLock(0, 5, TimeUnit.SECONDS)) { // 1초 대기, 최대 10초 락 유지
                try {
                    // Order 생성
                    Order order = orderWithOrderSeatSave(UUID.fromString(seatId), userId);

                    // TTL을 확인할 키 설정 (order.getId()를 제외)
                    String seatIdWithTTLPrefix = verifyTtlForSeat(seatId, scheduleId);

                    setTtlInLock(redisSeat, scheduleId, seatIdWithTTLPrefix, order);

                    return OrderResponse.from(order, redisSeat);
                } finally {
                    if (lock.isHeldByCurrentThread()) {
                        lock.unlock();
                    }
                }
            } else {
                throw new ApplicationException(LOCK_ACQUISITION_FAIL);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ApplicationException(LOCK_ACQUISITION_FAIL);
        } catch (JsonProcessingException e) {
            throw new ApplicationException(JSON_PROCESSING_EXCEPTION);
        }
    }

    private String verifyTtlForSeat(String seatId, String scheduleId) {
        String seatIdWithTTLPrefix = scheduleId + ":" + seatId; // 접두사 설정

        // SCAN을 사용하여 키가 존재하는지 확인
        boolean exists = redisService.keysStartingWith(seatIdWithTTLPrefix);

        if (exists) {
            throw new ApplicationException(TTL_ALREADY_EXISTS);
        }
        return seatIdWithTTLPrefix;
    }

    private void setTtlInLock(RedisSeat redisSeat, String scheduleId, String seatIdWithTTLPrefix, Order order) throws JsonProcessingException {
        // SeatIdWithTTL 설정 (order.getId. 포함)
        String SeatIdWithTTL = seatIdWithTTLPrefix +":"+ order.getId();

        // TTL 설정
        redisService.setTtl(SeatIdWithTTL, scheduleId, SEAT_LOCK_CACHE_EXPIRE_SECONDS,
            TimeUnit.SECONDS);

        // RedisSeat의 상태를 true로 설정하고 Redis에 저장
        redisSeat.setSeatState(true);

        // RedisSeat 객체를 JSON으로 직렬화하여 Redis에 저장
        String updatedRedisSeatJson = objectMapper.writeValueAsString(redisSeat);
        redisService.setValue("seat:" + scheduleId + ":" + redisSeat.getSeatId(), updatedRedisSeatJson);
    }


    private Order orderWithOrderSeatSave(UUID seatId, UUID userId) {
        ResponseEntity<CommonResponse<OrderInfoResponse>> orderInfo = performanceClient.getOrderInfo(
            String.valueOf(seatId));
        OrderInfoResponse orderData = orderInfo.getBody().getData();

        Order order = Order.create(
            userId,
            orderData.companyId(),
            orderData.performanceName(),
            LocalDateTime.now(),
            true,
            orderData.scheduleId()
        );

        // 좌석 정보 설정
        OrderSeat orderSeat = OrderSeat.create(
            orderData.seatId(),
            orderData.row(),
            orderData.col(),
            orderData.seatRate(),
            orderData.cost()
        );

        order.setOrderSeat(orderSeat);
        orderSeatRepository.save(orderSeat);
        orderRepository.save(order);

        return order;
    }


    @Transactional
    public void updateOrderStatus(UUID orderId, String status) {
        // TODO: 로직을 status success/fail 상태에 따라 분리하기

        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException(ORDER_NOT_FOUND.getMessage()));

        UUID scheduleId = order.getScheduleId();
        UUID seatId = order.getOrderSeat().getSeatId();
        String redisKey = "seat:"+scheduleId+":"+seatId;

        RedisSeat redisSeat = getRedisSeat(redisKey);

        updateOrderAndSeatStatus(redisSeat,order,redisKey);

//        String performanceId = "1";
//        eventApplicationService.publishOrderCompletedEvent(
//            OrderCompletedEvent.create(String.valueOf(order.getUserId()), performanceId));

    }

    private void updateOrderAndSeatStatus(RedisSeat redisSeat, Order order, String redisKey) {

        order.setOrderStatus(false);
        orderRepository.save(order);

        redisSeat.setSeatState(false);
        try {
            String updatedRedisSeatJson = objectMapper.writeValueAsString(redisSeat);
            redisService.setValue(redisKey, updatedRedisSeatJson);
        } catch (JsonProcessingException e) {
            throw new ApplicationException(JSON_PROCESSING_EXCEPTION);
        }
        
        //TODO : 결제 취소 구현시 redis or performanceDB 저장 실패 및 결제 취소 구현

        ResponseEntity<CommonResponse<SeatResponse>> savedPerformanceToDb
            = performanceClient.updateSeatState(UUID.fromString(redisSeat.getSeatId()),
            redisSeat.getSeatState());

        System.out.println("savedPerformanceToDb.getBody().getData().seatId() = " + savedPerformanceToDb.getBody().getData().seatId());
    }

    public void test() {
        // 예매 완료 이벤트 발행
        String userId = "1";
        String performanceId = "1";
        eventApplicationService.publishOrderCompletedEvent(
            OrderCompletedEvent.create(userId, performanceId));
    }

    public PaymentResponseDto orderInfoResponseToPayment(UUID orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ApplicationException(REQUEST_ORDER_INFORMATION_BY_PAYMENT_NOT_FOUND));

        return new PaymentResponseDto(order.getPerformanceName(),order.getScheduleId(),
            order.getOrderSeat().getSeatId(), (long) order.getOrderSeat().getCost(),order.getUserId());
    }

    public boolean verifyOrder(PaymentRequestDto requestDto) {
        //1. TTL 존재 여부 확인
        String ttlPrefix = requestDto.getScheduleId()+":"+requestDto.getSeatId();
        Boolean isExist = redisService.keysStartingWith(ttlPrefix);

        if(!isExist) {
            throw new ApplicationException(TTL_NOT_FOUND)
;        }
        //2. redis 에서 주문상태 확인
        String redisKey = "seat:"+ ttlPrefix;
        RedisSeat redisSeat = getRedisSeat(redisKey);
        if(!redisSeat.getSeatState()) {//레디스 주문정보가 false 일때
            throw new ApplicationException(ORDER_IN_REDIS_NOT_OCCUPIED);
        }

        //3. performanceDB 에서 주문 상태 확인
        ResponseEntity<CommonResponse<OrderInfoResponse>> orderInfoResponse = performanceClient.getOrderInfo(
            String.valueOf(requestDto.getSeatId()));

        if(orderInfoResponse.getBody().getData().seatState()) {
            throw new ApplicationException(THE_SEAT_ALREADY_PAID_BY_SOMEONE);
        }

        //TODO : 4. order entity에서 주문상태 확인
        return true;
    }

    public ResponseEntity<CommonResponse<SeatResponse>> updateSeatState(UUID seatId, Boolean seatState) {
        // PerformanceClient를 통해 좌석 상태를 업데이트
        return performanceClient.updateSeatState(seatId, seatState);
    }
}
