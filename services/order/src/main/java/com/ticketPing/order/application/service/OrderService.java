package com.ticketPing.order.application.service;

import com.ticketPing.order.application.dtos.OrderCreateDto;
import com.ticketPing.order.application.dtos.OrderInfoResponse;
import com.ticketPing.order.application.dtos.OrderResponse;
import com.ticketPing.order.application.dtos.UserReservationDto;
import com.ticketPing.order.application.dtos.temp.SeatResponse;
import com.ticketPing.order.domain.events.OrderCompletedEvent;
import com.ticketPing.order.domain.model.enums.OrderStatus;
import com.ticketPing.order.infrastructure.client.PerformanceClient;
import com.ticketPing.order.domain.model.entity.Order;
import com.ticketPing.order.domain.model.entity.OrderSeat;
import com.ticketPing.order.infrastructure.repository.OrderRepository;
import com.ticketPing.order.infrastructure.repository.OrderSeatRepository;
import com.ticketPing.order.infrastructure.service.RedisService;
import common.exception.ApplicationException;
import common.response.CommonResponse;
import dto.PaymentRequestDto;
import dto.PaymentResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.ticketPing.order.presentation.cases.exception.OrderExceptionCase.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final PerformanceClient performanceClient;
    private final EventApplicationService eventApplicationService;
    private final RedisService redisService;
    private final RedissonClient redissonClient;

    private final static int SEAT_LOCK_CACHE_EXPIRE_SECONDS = 30;
    private final static String TTL_PREFIX = "seat_ttl:";

    @Transactional
    public OrderResponse createOrder(OrderCreateDto orderCreateRequestDto, UUID userId) {
        String scheduleId = orderCreateRequestDto.scheduleId().toString();
        String seatId = orderCreateRequestDto.seatId().toString();
        String redisKey = "seat:" + scheduleId + ":" + seatId;

        SeatResponse seatResponse = Optional.ofNullable(redisService.getValueAsClass(redisKey, SeatResponse.class))
                .orElseThrow(() -> new ApplicationException(ORDER_FOR_PERFORMANCE_CACHE_NOT_FOUND));

        if(seatResponse.getSeatState()) {
            throw new ApplicationException(ORDER_ALREADY_OCCUPIED);
        }

        return redLockForSeat(userId, seatId, seatResponse, scheduleId);
    }

    private OrderResponse redLockForSeat(UUID userId, String seatId, SeatResponse redisSeat, String scheduleId) {
        String lockKey = "lock:" + seatId;
        RLock lock = redissonClient.getLock(lockKey);
        try {
            if (lock.tryLock(0, 5, TimeUnit.SECONDS)) { // 1초 대기, 최대 10초 락 유지
                try {
                    String seatIdWithTTLPrefix = verifyTtlForSeat(seatId, scheduleId);
                    Order order = orderWithOrderSeatSave(UUID.fromString(seatId), userId);
                    setTtlInLock(redisSeat, scheduleId, seatIdWithTTLPrefix, order);
                    return OrderResponse.from(order);
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
        }
    }

    private String verifyTtlForSeat(String seatId, String scheduleId) {
        String seatIdWithTTLPrefix = "seat_ttl:" + scheduleId + ":" + seatId;

        boolean exists = redisService.keysStartingWith(seatIdWithTTLPrefix);
        if (exists) {
            throw new ApplicationException(TTL_ALREADY_EXISTS);
        }

        return seatIdWithTTLPrefix;
    }

    private void setTtlInLock(SeatResponse seatResponse, String scheduleId, String seatIdWithTTLPrefix, Order order){
        String SeatIdWithTTL = seatIdWithTTLPrefix +":"+ order.getId();
        redisService.setValueWithTTL(SeatIdWithTTL, true, SEAT_LOCK_CACHE_EXPIRE_SECONDS);

        seatResponse.updateSeatState(true);
        redisService.setValue("seat:" + scheduleId + ":" + seatResponse.getSeatId(), seatResponse);
    }

    @Transactional
    public Order orderWithOrderSeatSave(UUID seatId, UUID userId) {
        OrderInfoResponse orderData = performanceClient.getOrderInfo(seatId.toString()).getBody().getData();

        Order order = Order.create(userId, orderData.companyId(), orderData.performanceName(),
                LocalDateTime.now(), OrderStatus.PENDING, orderData.scheduleId());
        Order savedOrder =  orderRepository.save(order);

        OrderSeat orderSeat = OrderSeat.create(orderData.seatId(), orderData.row(),
                orderData.col(), orderData.seatRate(), orderData.cost());
        savedOrder.setOrderSeat(orderSeat);
        return order;
    }

    @Transactional
    public void updateOrderStatus(UUID orderId, String status, UUID performanceId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ApplicationException(ORDER_NOT_FOUND));
        // success -> 예매 완료 -> seatStatus : true -> counter -1, ttl 삭제
        if(status.equals("success")) {
            UUID scheduleId = order.getScheduleId();
            UUID seatId = order.getOrderSeat().getSeatId();
            performanceClient.updateSeatState(order.getOrderSeat().getSeatId(), true);
            //redis ttl 삭제
            String ttlRedisKey = TTL_PREFIX + scheduleId + ":" + seatId + ":" + orderId;
            redisService.deleteKey(ttlRedisKey); // 키 삭제
            //counter -1
            redisService.decreaseCounter(performanceId);
            //kafka
            eventApplicationService.publishOrderCompletedEvent(
                    OrderCompletedEvent.create(String.valueOf(order.getUserId()), performanceId.toString()));
        }

    }

    public PaymentResponseDto orderInfoResponseToPayment(UUID orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ApplicationException(REQUEST_ORDER_INFORMATION_BY_PAYMENT_NOT_FOUND));

        return new PaymentResponseDto(order.getPerformanceName(),order.getScheduleId(),
            order.getOrderSeat().getSeatId(), (long) order.getOrderSeat().getCost(),order.getUserId());
    }

    public boolean verifyOrder(PaymentRequestDto requestDto) {
        String ttlPrefix = requestDto.getScheduleId()+":"+requestDto.getSeatId();
        Boolean hasMultipleKeys = redisService.hasMultipleKeysStartingWith(ttlPrefix);
        //2.TTL prefix 중복 여부
        if (hasMultipleKeys) {
            return false;
        }
        Boolean isExist = redisService.keysStartingWith(ttlPrefix);
        if(!isExist) {
           return false;
        }
        // performanceDB 에서 주문 상태 확인
        if (!checkSeatOrderStatus(requestDto)) {
            return false;
        }
        return true;
    }

    private boolean checkSeatOrderStatus(PaymentRequestDto requestDto) {
        ResponseEntity<CommonResponse<OrderInfoResponse>> orderInfoResponse = performanceClient.getOrderInfo(
            String.valueOf(requestDto.getSeatId()));

        if(orderInfoResponse.getBody().getData().seatState()) {
            return false;
        }

        //TODO : 4. order entity soft delete 된것 제외 seatId, scheduleId가 같은 것이 있으면 false
//        List<Order> orderList = orderRepository.findAll();
//        int cnt = 0;
//        for(Order order : orderList) {
//
//            if(order.getOrderSeat().getSeatId().equals(requestDto.getSeatId())
//                && order.getScheduleId().equals(requestDto.getScheduleId())) {
//                cnt++;
//            }
//            if(cnt >= 2) {
//                return false;
//            }
//        }
        return true;
    }

    @Transactional(readOnly = true)
    public List<UserReservationDto> getUserReservation(UUID userId) {
        List<Order> orders = orderRepository.findByUserId(userId);

        if (orders.isEmpty()) {
            throw new ApplicationException(ORDER_OF_USER_NOT_FOUND);
        }

        List<UserReservationDto> userReservationDtos = new ArrayList<>();

        for (Order order : orders) {
            // OrderSeat 객체를 가져옵니다.
            OrderSeat orderSeat = order.getOrderSeat();

            // OrderSeat이 null이 아닐 경우 DTO로 매핑
            if (orderSeat != null) {
                UserReservationDto dto = UserReservationDto.from(order, orderSeat);
                userReservationDtos.add(dto);
            }
        }

        return userReservationDtos; // UserReservationDto 리스트 반환
    }

}
