package com.ticketPing.order.application.service;

import static com.ticketPing.order.presentation.cases.exception.OrderExceptionCase.JSON_PROCESSING_EXCEPTION;
import static com.ticketPing.order.presentation.cases.exception.OrderExceptionCase.LOCK_ACQUISITION_FAIL;
import static com.ticketPing.order.presentation.cases.exception.OrderExceptionCase.ORDER_ALREADY_OCCUPIED;
import static com.ticketPing.order.presentation.cases.exception.OrderExceptionCase.ORDER_FOR_PERFORMANCE_CACHE_NOT_FOUND;
import static com.ticketPing.order.presentation.cases.exception.OrderExceptionCase.ORDER_NOT_FOUND;
import static com.ticketPing.order.presentation.cases.exception.OrderExceptionCase.ORDER_OF_USER_NOT_FOUND;
import static com.ticketPing.order.presentation.cases.exception.OrderExceptionCase.REQUEST_ORDER_INFORMATION_BY_PAYMENT_NOT_FOUND;
import static com.ticketPing.order.presentation.cases.exception.OrderExceptionCase.TTL_ALREADY_EXISTS;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketPing.order.application.dtos.OrderCreateDto;
import com.ticketPing.order.application.dtos.OrderPerformanceDetails;
import com.ticketPing.order.application.dtos.OrderResponse;
import com.ticketPing.order.application.dtos.OrderInfoResponse;
import com.ticketPing.order.application.dtos.OrderSeatInfo;
import com.ticketPing.order.application.dtos.UserReservationDto;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
        if(status.equals("fail")) return;
        // TODO: 로직을 status success/fail 상태에 따라 분리하기
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ApplicationException(ORDER_NOT_FOUND));

        UUID scheduleId = order.getScheduleId();
        UUID seatId = order.getOrderSeat().getSeatId();
        String ttlRedisKey = scheduleId+":"+seatId+":"+orderId;
        redisService.deleteKey(ttlRedisKey); // 키 삭제

        ResponseEntity<CommonResponse<SeatResponse>> savedPerformanceToDb
            = performanceClient.updateSeatState(order.getOrderSeat().getSeatId(),
            order.getOrderStatus());

//        String performanceId = "1";
//        eventApplicationService.publishOrderCompletedEvent(
//            OrderCompletedEvent.create(String.valueOf(order.getUserId()), performanceId));

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

    public OrderPerformanceDetails getAllSeats() {
        List<String> redisSeatKeyList = redisService.getKeysStartingWith("seat:");
        // 첫 번째 키를 기준으로 seatId와 scheduleId 추출
        String input = redisSeatKeyList.get(0);
        String regex = "seat:([0-9a-fA-F-]+):([0-9a-fA-F-]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        String scheduleId = null;
        String seatId = null;
        if (matcher.find()) {
            seatId = matcher.group(2); // 첫 번째 그룹: seatId
            scheduleId = matcher.group(1); // 두 번째 그룹: scheduleId
        }

        ResponseEntity<CommonResponse<OrderInfoResponse>> orderInfo = performanceClient.getOrderInfo(
            String.valueOf(seatId)
        );
        OrderInfoResponse orderData = orderInfo.getBody().getData();
        // OrderPerformanceDetails 생성

        return initializeOrderPerformanceDetails(orderData, scheduleId, redisSeatKeyList);
    }

    private OrderPerformanceDetails initializeOrderPerformanceDetails(OrderInfoResponse orderData,
        String scheduleId, List<String> redisSeatKeyList) {
        OrderPerformanceDetails orderPerformanceDetails = OrderPerformanceDetails.create(
            orderData.performanceHallName(),
            orderData.performanceName(),
            orderData.startTime()
        );
        ResponseEntity<CommonResponse<List<SeatResponse>>> seatResponseList = performanceClient.getAllScheduleSeats(
            UUID.fromString(scheduleId)
        );
        List<SeatResponse> seatResponse = seatResponseList.getBody().getData();

        for (String seatKey : redisSeatKeyList) {
            String redisSeatJson = redisService.getValue(seatKey);

            if (redisSeatJson != null) {
                RedisSeat redisSeat = getRedisSeat(seatKey);
                OrderSeatInfo orderSeatInfo = OrderSeatInfo.from(redisSeat);
                // RedisSeat의 상태와 SeatResponse의 상태 비교
                for (SeatResponse seatRes : seatResponse) {
                    if (redisSeat.getSeatState() || seatRes.seatState()) {
                        orderSeatInfo.updateSeatState(true);
                    } else {
                        orderSeatInfo.updateSeatState(false);
                    }
                }
                orderPerformanceDetails.addList(orderSeatInfo);
            }
        }
        return orderPerformanceDetails;
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
