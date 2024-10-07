package com.ticketPing.payment.application.dto;

import com.ticketPing.payment.domain.enums.PayType;
import com.ticketPing.payment.domain.model.Payment;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

class PaymentRequestDtoTest {

    @Test
    void test() {
        PaymentRequestDto spy = spy(PaymentRequestDto.class);
        ReflectionTestUtils.setField(spy, "bookingId", UUID.randomUUID());
        ReflectionTestUtils.setField(spy, "companyName", "companyName");
        ReflectionTestUtils.setField(spy, "customerEmail", "customerEmail");
        ReflectionTestUtils.setField(spy, "customerName", "customerName");
        ReflectionTestUtils.setField(spy, "payType", PayType.CARD);
        ReflectionTestUtils.setField(spy, "performanceName", "performanceName");
        ReflectionTestUtils.setField(spy, "amount", 50000L);

        Payment entity = spy.toEntity();

        assertNotNull(entity.isPaySuccessYn());

    }
}