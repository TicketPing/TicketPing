package com.ticketPing.payment.infrastructure.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class TossPaymentConfig {

    @Value("${payment.toss.test_client_api_key}")
    private String testClientApiKey;
    @Value("${payment.toss.test_secret_api_key}")
    private String testSecretKey;
    @Value("${payment.toss.success_url}")
    private String successUrl;
    @Value("${payment.toss.fail_url}")
    private String failUrl;
    //토스에 결제 승인 요청 보낼 URL
    @Value("${payment.toss.origin_url}")
    private String tossOriginUrl;
}
