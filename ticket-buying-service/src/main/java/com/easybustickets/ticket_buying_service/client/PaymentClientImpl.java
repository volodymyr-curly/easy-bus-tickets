package com.easybustickets.ticket_buying_service.client;

import com.easybustickets.ticket_buying_service.dto.payment.PaymentRequest;
import com.easybustickets.ticket_buying_service.dto.payment.PaymentResponse;
import com.easybustickets.ticket_buying_service.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static com.easybustickets.ticket_buying_service.exception.ExceptionMessage.INTERNAL_SERVER_ERROR_MESSAGE;
import static com.easybustickets.ticket_buying_service.exception.ExceptionMessage.PAYMENT_NOT_FOUND_MESSAGE;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentClientImpl implements PaymentClient{

    private final WebClient.Builder webClientBuilder;

    @Value("${payment-service.url}")
    private String paymentServiceUrl;

    @Value("${payment-status-service.url}")
    private String paymentStatusServiceUrl;

    public Mono<PaymentResponse> createPayment(String personName, BigDecimal price) {
        return webClientBuilder.build()
                .post()
                .uri(paymentServiceUrl)
                .bodyValue(new PaymentRequest(personName, price))
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    log.error("Internal Server Error");
                    return Mono.error(new RuntimeException(INTERNAL_SERVER_ERROR_MESSAGE));
                })
                .bodyToMono(PaymentResponse.class);
    }

    public Mono<PaymentResponse> getPayment(String id) {
        return webClientBuilder.build()
                .get()
                .uri(paymentStatusServiceUrl, id)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    log.error("Payment with id = {} not found", id);
                    return Mono.error(new EntityNotFoundException(PAYMENT_NOT_FOUND_MESSAGE));
                })
                .bodyToMono(PaymentResponse.class);
    }
}
