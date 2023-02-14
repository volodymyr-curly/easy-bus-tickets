package com.easybustickets.payment_status_service.controller;

import com.easybustickets.payment_status_service.dto.PaymentResponse;
import com.easybustickets.payment_status_service.model.Payment;
import com.easybustickets.payment_status_service.service.PaymentStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/payment-status")
@RequiredArgsConstructor
public class PaymentStatusController {

    private final PaymentStatusService statusService;

    @PostMapping("/{paymentId}")
    public PaymentResponse createPaymentStatus(@PathVariable String paymentId) {
        log.debug("Set status to payment with paymentId={}", paymentId);
        Payment payment = statusService.addPaymentStatus(paymentId);
        log.debug(payment.toString());
        return PaymentResponse.builder()
                .paymentId(payment.getPaymentId())
                .status(payment.getStatus())
                .build();
    }

    @PutMapping("/{paymentId}")
    public PaymentResponse updatePaymentStatus(@PathVariable String paymentId) {
        log.debug("Update payment with paymentId={}", paymentId);
        Payment updatedPayment = statusService.updatePaymentStatus(paymentId);
        return PaymentResponse.builder()
                .paymentId(updatedPayment.getPaymentId())
                .status(updatedPayment.getStatus())
                .build();
    }
}
