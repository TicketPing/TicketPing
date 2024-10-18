package com.ticketPing.payment.application.service;

import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.ticketPing.payment.application.dto.StripeForClientResponseDto;
import com.ticketPing.payment.application.dto.StripeResponseDto;
import com.ticketPing.payment.domain.model.entity.Payment;
import com.ticketPing.payment.infrastructure.client.OrderClient;
import com.ticketPing.payment.infrastructure.configuration.StripePaymentConfig;
import com.ticketPing.payment.infrastructure.redis.RedisService;
import com.ticketPing.payment.infrastructure.repository.PaymentRepository;
import com.ticketPing.payment.presentation.request.PaymentStripeRequestDto;
import common.exception.ApplicationException;
import dto.PaymentRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.ticketPing.payment.presentation.cases.PaymentErrorCase.*;

@Service
public class StripePaymentService {

    public final String CURRENCY = "krw";
    private final StripeClient client;
    private final PaymentRepository repository;
    private final OrderClient orderClient;
    private final RedisService redisService;

    @Autowired
    public StripePaymentService(StripePaymentConfig config, PaymentRepository repository, OrderClient orderClient, RedisService redisService) {
        this.client = new StripeClient(config.getSecretKey());
        this.repository = repository;
        this.orderClient = orderClient;
        this.redisService = redisService;
    }

    //결제 요청
    public StripeForClientResponseDto payment(UUID orderId) {
            PaymentStripeRequestDto requestDto = getOrderInfo(orderId);
            verifyOrder(requestDto);
            PaymentIntent paymentIntent = createPi(requestDto);
            StripeResponseDto responseDto = new StripeResponseDto(paymentIntent, orderId);
            Payment payment = new Payment(responseDto);
            repository.save(payment);
            return new StripeForClientResponseDto(paymentIntent.getClientSecret(), paymentIntent.getId());
    }

    // 중복 예매 검증 (공연명, 스케쥴, 자리)
    private void verifyOrder(PaymentStripeRequestDto requestDto) {
        PaymentRequestDto request = PaymentRequestDto.field(
                requestDto.getPerformanceScheduleId(),
                requestDto.getSeatId());
        if(!orderClient.verifyOrder(request)) throw new ApplicationException(ORDER_VERIFY_FAIL);
    }

    //orderId로 Order에 orderInfo 데이터 요청
    private PaymentStripeRequestDto getOrderInfo (UUID orderId) {
        return PaymentStripeRequestDto.get(orderClient.getOrderInfo(orderId));
    }

    //create paymentIntent
    private PaymentIntent createPi (PaymentStripeRequestDto requestDto) {
        try {
            PaymentIntentCreateParams params = getPaymentIntentCreateParams(
                    requestDto.getAmount(),
                    requestDto.getPerformanceName(),
                    requestDto.getPerformanceScheduleId(),
                    requestDto.getSeatId(),
                    requestDto.getUserId()
            );
            return client.paymentIntents().create(params);
        } catch (StripeException e) {
            throw new ApplicationException(PI_CREATE_FAIL);
        }
    }

    private PaymentIntentCreateParams getPaymentIntentCreateParams(Long amount, String performanceName, UUID performanceScheduleId, UUID seatId, UUID userId) {
        return PaymentIntentCreateParams.builder()
                .setCurrency(CURRENCY)
                .setAmount(amount)
                .setDescription(performanceName + ":" + performanceScheduleId + ":" + seatId + ":" + userId)
                .setSetupFutureUsage(PaymentIntentCreateParams.SetupFutureUsage.OFF_SESSION)
                .build();
    }

    // TTL 확인
    public void verifyTtl(UUID orderId) {
        // Todo : Redis에 orderId로 TTL 확인 (key = scheduleId:seatId:orderId)
        Payment payment = repository.findByOrderId(orderId)
                .orElseThrow(() -> new ApplicationException(PAYMENT_NOT_FOUND));
        String redisKey = payment.getOrderInfo().getPerformanceScheduleId() + ":" + payment.getOrderInfo().getSeatId() + ":" + orderId;
        redisCheckTTL(redisKey);
    }

    private void redisCheckTTL(String redisKey) {
        Long ttl = redisService.getTTL(redisKey);
        if(ttl <= 30L) {
            // 30s보다 작거나, -1(ttl 설정X), -2(존재하지 않는 키) -> fail
            throw new ApplicationException(TTL_VERIFY_FAIL);
        }
    }


    public String updateStatus(String paymentIntentId) {

        try {
        //paymentIntentId 값으로 orderId값 찾아오기
        Payment payment= repository.findByPaymentIntentId(paymentIntentId)
                .orElseThrow(() -> new ApplicationException(PAYMENT_NOT_FOUND));
        UUID orderId = payment.getOrderInfo().getOrderId();
        UUID userId = payment.getUserId();
        // Todo : paymentIntentId로 paymentIntent 찾아오기
        PaymentIntent paymentIntent = client.paymentIntents().retrieve(paymentIntentId);
        payment.setStatus(paymentIntent.getStatus());
        // Todo : status = succeeded 일 경우 예매 호출
//        if(payment.getStatus().equals("succeeded")) {
//            try {
//                reservationClient.updateOrderStatus(orderId, "success");
//            } catch (FeignException fe) {
//                System.err.println("FeignClient success call failed = paymentIntentId : " + paymentIntentId);
//                fe.printStackTrace();
//                throw new ApplicationException(FEIGN_CLIENT_FAIL);
//            }
//        } else {
//            try {
//                reservationClient.updateOrderStatus(orderId, "fail");
//            } catch (FeignException fe) {
//                System.err.println("FeignClient fail call failed = paymentIntentId : " + paymentIntentId);
//                fe.printStackTrace();
//                throw new ApplicationException(FEIGN_CLIENT_FAIL);
//            }
//        }
        repository.save(payment);
        return paymentIntent.getStatus();
        } catch (Exception e) {
            System.err.println("Error updating status for paymentIntentId: " + paymentIntentId);
            e.printStackTrace(); // 로그에 스택 트레이스를 출력
            throw new ApplicationException(STATUS_UPDATE_FAIL);
        }
    }
}
