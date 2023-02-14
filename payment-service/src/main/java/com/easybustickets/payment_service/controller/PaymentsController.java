package com.easybustickets.payment_service.controller;

import com.easybustickets.payment_service.dto.PaymentRequest;
import com.easybustickets.payment_service.dto.PaymentResponse;
import com.easybustickets.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentsController {

    private final PaymentService paymentService;

    @PostMapping
    public PaymentResponse createPayment(@RequestBody PaymentRequest request) {
          return paymentService.generatePaymentResponse(request.getPersonName(), request.getTicketPrice());
    }
}
