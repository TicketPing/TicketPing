package com.ticketPing.payment.application.service;

import com.ticketPing.payment.application.dto.PaymentRequestDto;
import com.ticketPing.payment.application.dto.PaymentResponseDto;
import com.ticketPing.payment.domain.enums.PayType;
import com.ticketPing.payment.domain.model.Payment;
import com.ticketPing.payment.domain.repository.PaymentRepository;
import com.ticketPing.payment.infrastructure.configuration.TossPaymentConfig;
import com.ticketPing.payment.infrastructure.repository.PaymentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.UUID;

import static com.ticketPing.payment.domain.enums.PayType.CARD;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentJpaRepository paymentRepository;
    private final TossPaymentConfig tossPaymentConfig;


    @Transactional
    public PaymentResponseDto requestPayments(PaymentRequestDto paymentRequestDto) {
        Long amount = paymentRequestDto.getAmount();
        PayType payType = paymentRequestDto.getPayType();
        String customerEmail = paymentRequestDto.getCustomerEmail();
        //String performanceName = paymentRequestDto.getPerformanceName();

        if(amount == null || amount != 50000) {
            throw new IllegalArgumentException("가격 오류");
        }
        if(!payType.equals(CARD)) {
            throw new IllegalArgumentException("결제 타입 오류");
        }
        PaymentResponseDto paymentResponseDto;
        try {
            Payment payment = paymentRequestDto.toEntity();
            paymentRepository.save(payment);
            //Todo: 유저 존재 확인
            paymentResponseDto = payment.toResponseDto();
            paymentResponseDto.setSuccessUrl(tossPaymentConfig.getSuccessUrl());
            paymentResponseDto.setFailUrl(tossPaymentConfig.getFailUrl());
            return paymentResponseDto;
        } catch (Exception e) {
            throw new IllegalArgumentException("requestPayments 오류");
        }
    }

//    public void verifyRequest(String paymentKey, UUID orderId, Long amount) {
//        paymentRepository.findByOrderId(orderId)
//                .ifPresentOrElse(
//                        P -> {
//                            if(P.getAmount() == amount) {
//                                P.setPaymentKey(paymentKey);
//                            } else {
//                                throw new IllegalArgumentException("verifyRequest error");
//                            }
//                        }, () -> {
//                            throw new IllegalArgumentException("verifyRequest2 error");
//                        }
//                );
//    }
//
//    public String requestFinalPayment(String paymentKey, UUID orderId, Long amount) {
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//
//        String testSecretApiKey = tossPaymentConfig.getTestSecretKey() + ":";
//        String encodedAuth = new String(Base64.getEncoder().encode(testSecretApiKey.getBytes(StandardCharsets.UTF_8)));
//
//        headers.setBasicAuth(encodedAuth);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//
//        JSONObject param = new JSONObject();
//        param.put("orderId", orderId);
//        param.put("amount", amount);
//
//        return restTemplate.postForEntity(
//                tossPaymentConfig.getTossOriginUrl() + paymentKey,
//                new HttpEntity<>(param, headers),
//                String.class
//        ).getBody();
//
//    }
}
