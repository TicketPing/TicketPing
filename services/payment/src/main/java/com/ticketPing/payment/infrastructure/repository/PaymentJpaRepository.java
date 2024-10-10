package com.ticketPing.payment.infrastructure.repository;

import com.ticketPing.payment.domain.model.Payment;
import com.ticketPing.payment.domain.repository.PaymentRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentJpaRepository extends JpaRepository<Payment, UUID>, PaymentRepository {

    Optional<Payment> findByPaymentIntentId(String paymentIntentId);
}
