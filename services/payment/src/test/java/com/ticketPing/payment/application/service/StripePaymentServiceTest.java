package com.ticketPing.payment.application.service;

import com.stripe.StripeClient;
import com.ticketPing.payment.infrastructure.configuration.StripePaymentConfig;
import com.ticketPing.payment.infrastructure.repository.PaymentRepository;

import static org.assertj.core.api.Assertions.assertThat;


class StripePaymentServiceTest {
    private StripePaymentService stripePaymentService;
    private PaymentRepository repository;
   // private OrderClient reservationClient;
    private StripePaymentConfig config;
    private StripeClient client;

//    @BeforeEach
//    void setUp() {
//        stripePaymentService = new StripePaymentService(config, repository, reservationClient);
//        client = new StripeClient(config.getSecretKey());
//    }


//    @Test
//    @DisplayName("결제 테스트")
//    void paymentTest() {
//        StripeResponseDto payment = stripePaymentService.payment(UUID.randomUUID(),1000L, "test code", "test@test.com");
//        //String receiptEmail = payment.getReceiptEmail();
//        //assertThat(receiptEmail).isEqualTo("test@test.com");
//        assertThat(payment.getAmount()).isEqualTo(1000L);
//        assertThat(payment.getPerformanceName()).isEqualTo("test code");
//        assertThat(payment.getStatus()).isEqualTo("requires_payment_method");
//
//        System.out.println(payment);
//    }

}