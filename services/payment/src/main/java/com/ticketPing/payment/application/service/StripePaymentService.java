package com.ticketPing.payment.application.service;

import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.ticketPing.payment.application.dto.StripeResponseDto;
import com.ticketPing.payment.domain.model.Payment;
import com.ticketPing.payment.infrastructure.configuration.StripePaymentConfig;
import com.ticketPing.payment.infrastructure.repository.PaymentJpaRepository;
import com.ticketPing.payment.presentation.request.StripeRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

@Service
public class StripePaymentService {

    public final String CURRENCY = "krw";
    private final StripeClient client;
    private final PaymentJpaRepository repository;


    public StripePaymentService(StripePaymentConfig config, PaymentJpaRepository repository) {
        this.client = new StripeClient(config.getSecretKey());
        this.repository = repository;
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
            throw new IllegalArgumentException(e.getMessage(), e);
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
}
