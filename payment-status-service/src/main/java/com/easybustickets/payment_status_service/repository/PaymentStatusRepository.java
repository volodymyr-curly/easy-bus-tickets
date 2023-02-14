package com.easybustickets.payment_status_service.repository;

import com.easybustickets.payment_status_service.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentStatusRepository extends JpaRepository<Payment, String> {

    Optional<Payment> findByPaymentId(String paymentId);

    Boolean existsByPaymentId(String paymentId);
}
