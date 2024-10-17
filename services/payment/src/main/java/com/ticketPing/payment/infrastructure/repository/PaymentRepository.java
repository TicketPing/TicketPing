package com.ticketPing.payment.infrastructure.repository;

import com.ticketPing.payment.domain.model.entity.Payment;
import com.ticketPing.payment.domain.repository.PaymentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID>, PaymentRepositoryCustom {

    Optional<Payment> findByPaymentIntentId(String paymentIntentId);

    Optional<Payment> findByOrderId(UUID orderId);
}
