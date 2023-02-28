package com.easybustickets.ticket_buying_service.client;

import com.easybustickets.ticket_buying_service.dto.payment.PaymentResponse;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface PaymentClient {

    Mono<PaymentResponse> createPayment(String personName, BigDecimal price);

    Mono<PaymentResponse> getPayment(String id);
}
