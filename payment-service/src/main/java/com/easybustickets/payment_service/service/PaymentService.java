package com.easybustickets.payment_service.service;

import com.easybustickets.payment_service.dto.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final WebClient.Builder webClientBuilder;

    public PaymentResponse generatePaymentResponse(String personName, BigDecimal ticketPrice) {
        String paymentId = generatePaymentId(personName, ticketPrice);
        return  webClientBuilder.build().post()
                .uri("http://payment-status-service/api/payment-status/" + paymentId)
                .retrieve()
                .bodyToMono(PaymentResponse.class)
                .block();
    }


    private String generatePaymentId(String personName, BigDecimal ticketPrice) {
        return UUID.randomUUID().toString()
                .concat(personName)
                .concat(ticketPrice.toString())
                .concat(LocalDateTime.now().toString());
    }
}
