package com.easybustickets.payment_status_service.service;

import com.easybustickets.payment_status_service.model.Payment;
import com.easybustickets.payment_status_service.model.PaymentStatus;
import com.easybustickets.payment_status_service.repository.PaymentStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Random;

import static com.easybustickets.payment_status_service.exception.ExceptionMessage.PAYMENT_EXISTS_MESSAGE;
import static com.easybustickets.payment_status_service.exception.ExceptionMessage.PAYMENT_NOT_FOUND_MESSAGE;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class PaymentStatusService {

    private final PaymentStatusRepository statusRepository;

    public Payment addPaymentStatus(String paymentId) {
        if (statusRepository.existsByPaymentId(paymentId)) {
            log.error(PAYMENT_EXISTS_MESSAGE);
            throw new EntityExistsException(PAYMENT_EXISTS_MESSAGE);
        }

        String paymentStatus = generateStatus();
        return statusRepository.save(Payment.builder()
                .paymentId(paymentId)
                .status(paymentStatus)
                .build());
    }

    public Payment updatePaymentStatus(String paymentId) {
        Payment paymentToUpdate = statusRepository.findByPaymentId(paymentId).orElseThrow(() -> {
            log.error(PAYMENT_NOT_FOUND_MESSAGE);
            throw new EntityNotFoundException(PAYMENT_NOT_FOUND_MESSAGE);
        });

        String updatedStatus = generateStatus();
        return statusRepository.save(Payment.builder()
                .id(paymentToUpdate.getId())
                .paymentId(paymentId)
                .status(updatedStatus)
                .build());
    }

    private String generateStatus() {
        PaymentStatus[] values = PaymentStatus.values();
        int random = new Random().nextInt(values.length);
        return values[random].name();
    }

    public Payment getPaymentStatus(String paymentId) {
        return statusRepository.findByPaymentId(paymentId).orElseThrow(() -> {
            log.error(PAYMENT_NOT_FOUND_MESSAGE);
            throw new EntityNotFoundException(PAYMENT_NOT_FOUND_MESSAGE);
        });
    }
}
