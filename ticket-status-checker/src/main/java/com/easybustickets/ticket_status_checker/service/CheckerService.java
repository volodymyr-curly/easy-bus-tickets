package com.easybustickets.ticket_status_checker.service;

import com.easybustickets.ticket_status_checker.dto.Ticket;
import com.easybustickets.ticket_status_checker.model.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CheckerService {

    private final WebClient.Builder webClientBuilder;

    @Value("${ticket-service.base-url}")
    private String ticketServiceBaseUrl;

    @Value("${route-service.base-url}")
    private String routeServiceBaseUrl;


    @Scheduled(initialDelay = 5000L, fixedDelay = 5000L)
    public void examinePayments() {
        getTicketsWithStatus(PaymentStatus.NEW.name()).forEach(ticket -> {
            log.info("Begin changing status");
            String updateStatus = webClientBuilder.build().patch()
                    .uri(ticketServiceBaseUrl+"edit/status")
                    .bodyValue(ticket)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info("End changing status");

            if (updateStatus.equals(PaymentStatus.DONE.name())) {
                log.info("Begin decrement tickets amount");
                webClientBuilder.build()
                        .patch()
                        .uri(routeServiceBaseUrl+ticket.getRouteId())
                        .retrieve()
                        .bodyToMono(Void.class)
                        .block();
                log.info("End decrement tickets amount");
            }
        });
    }

    private List<Ticket> getTicketsWithStatus(String paymentStatus) {
        return webClientBuilder.build().get()
                .uri(ticketServiceBaseUrl+"status/{status}", paymentStatus)
                .retrieve()
                .bodyToFlux(Ticket.class)
                .collectList()
                .block();
    }
}
