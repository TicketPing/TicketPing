package com.ticketPing.payment.application.service;

import com.stripe.Stripe;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethodDomain;
import com.stripe.net.RequestOptions;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentMethodDomainCreateParams;
import com.ticketPing.payment.application.dto.StripeResponseDto;
import com.ticketPing.payment.domain.model.Payment;
import com.ticketPing.payment.infrastructure.client.ReservationClient;
import com.ticketPing.payment.infrastructure.configuration.StripePaymentConfig;
import com.ticketPing.payment.infrastructure.repository.PaymentJpaRepository;
import com.ticketPing.payment.presentation.request.StripeRequestDto;
import common.exception.ApplicationException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Arrays;
import java.util.UUID;

import static com.ticketPing.payment.cases.PaymentErrorCase.*;

@Service
public class StripePaymentService {

    public final String CURRENCY = "krw";
    private final StripeClient client;
    private final PaymentJpaRepository repository;
    private final ReservationClient reservationClient;
    private final StripePaymentConfig config;


    @Autowired
    public StripePaymentService(StripePaymentConfig config, PaymentJpaRepository repository, ReservationClient reservationClient) {
        this.config = config;
        this.client = new StripeClient(config.getSecretKey());
        this.repository = repository;
        this.reservationClient = reservationClient;
    }

    public StripeResponseDto payment(UUID orderId, StripeRequestDto requestDto) {
        try {
            PaymentIntentCreateParams params = getPaymentIntentCreateParams(
                    requestDto.getAmount(), requestDto.getPerformanceName(), requestDto.getPerformanceTime(), requestDto.getSeatInfo(), requestDto.getUserEmail());
            PaymentIntent paymentIntent = client.paymentIntents().create(params);
            StripeResponseDto responseDto = new StripeResponseDto(paymentIntent.getId(),
                    orderId,
                    paymentIntent.getReceiptEmail(),
                    paymentIntent.getStatus(),
                    paymentIntent.getAmount(),
                    paymentIntent.getClientSecret(),
                    paymentIntent.getDescription(),
                    paymentIntent.getCreated());

            //dto -> entity
            Payment payment = new Payment(responseDto);
            //Todo : 중복 예매 검증 -> 공연명과 공연일자와 자리가 같을 경우?? -> 예매 파트에서? 결제에서도 한번 더?

            //db 저장
            repository.save(payment);
            return responseDto;
        } catch (StripeException e) {
            throw new ApplicationException(PAYMENT_INTENT_FAIL);
        }
    }

    private PaymentIntentCreateParams getPaymentIntentCreateParams(Long amount, String performanceName, String performanceTime, String seatInfo, String email) {
        return PaymentIntentCreateParams.builder()
                .setCurrency(CURRENCY)
                .setAmount(amount)
                .setDescription(performanceName + " , " + performanceTime + " , " + seatInfo)
                .setReceiptEmail(email)
                .setSetupFutureUsage(PaymentIntentCreateParams.SetupFutureUsage.OFF_SESSION)
                .build();
    }

    public void updateStatus(String paymentIntentId) {
        try {
        //paymentIntentId 값으로 orderId값 찾아오기
        Payment payment= repository.findByPaymentIntentId(paymentIntentId)
                .orElseThrow(() -> new ApplicationException(PAYMENT_NOT_FOUND));
        UUID orderId = payment.getOrderInfo().getOrderId();
        // Todo : 일단 succeeded 처리
        payment.setStatus("succeeded");
        //status = succeeded 일 경우 예매 호출
        if(payment.getStatus().equals("succeeded")) {
            try {
                reservationClient.updateOrderStatus(orderId, "결제 완료");
            } catch (FeignException fe) {
                System.err.println("FeignClient success call failed = paymentIntentId : " + paymentIntentId);
                fe.printStackTrace();
                throw new ApplicationException(FEIGN_CLIENT_FAIL);
            }
        } else if(payment.getStatus().equals("canceled")) {
            try {
                reservationClient.updateOrderStatus(orderId, "결제 실패");
            } catch (FeignException fe) {
                System.err.println("FeignClient fail call failed = paymentIntentId : " + paymentIntentId);
                fe.printStackTrace();
                throw new ApplicationException(FEIGN_CLIENT_FAIL);
            }
        }
        } catch (Exception e) {
            System.err.println("Error updating status for paymentIntentId: " + paymentIntentId);
            e.printStackTrace(); // 로그에 스택 트레이스를 출력
            throw new ApplicationException(STATUS_UPDATE_FAIL);
        }
    }
}
