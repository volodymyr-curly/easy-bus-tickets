package com.easybustickets.ticket_buying_service.service;

import com.easybustickets.ticket_buying_service.dto.PaymentRequest;
import com.easybustickets.ticket_buying_service.dto.PaymentResponse;
import com.easybustickets.ticket_buying_service.exception.EntityNotFoundException;
import com.easybustickets.ticket_buying_service.exception.TicketAmountException;
import com.easybustickets.ticket_buying_service.model.Ticket;
import com.easybustickets.ticket_buying_service.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static com.easybustickets.ticket_buying_service.exception.ExceptionMessage.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final WebClient.Builder webClientBuilder;

    @Value("${routes-service.url}")
    private String routeServiceUrl;

    @Value("${payment-service.url}")
    private String paymentServiceUrl;

    @Override
    public Mono<Ticket> addTicket(Mono<Ticket> ticket) {
        return ticket
                .flatMap(t -> createPayment(t.getPersonName(), t.getPrice())
                        .flatMap(paymentResponse -> {
                            t.setPaymentId(paymentResponse.getPaymentId());
                            return setSeatNumber(t, paymentResponse);
                        }))
                .flatMap(ticketRepository::save);
    }

    @Override
    public Mono<Ticket> getTicket(String id) {
        return ticketRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException(TICKET_NOT_FOUND_MESSAGE)))
                .onErrorResume(throwable -> {
                    log.error("Failed to get ticket with id={}", id);
                    return Mono.error(throwable);
                });
    }

    private Mono<PaymentResponse> createPayment(String personName, BigDecimal price) {
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

    private Mono<Ticket> setSeatNumber(Ticket ticket, PaymentResponse paymentResponse) {
        return Mono.just(ticket)
                .filter(t -> paymentResponse.getStatus().equals("DONE"))
                .flatMap(t -> decrementAvailableTickets(t.getRouteId())
                        .flatMap(seatNumber -> {

                            if (seatNumber <= 0) {
                                return Mono.error(new TicketAmountException(TICKET_AMOUNT_MESSAGE));

                            } else {
                                t.setSeatNumber(seatNumber);
                                return Mono.just(t);
                            }
                        }))
                .switchIfEmpty(Mono.just(ticket));
    }

    private Mono<Integer> decrementAvailableTickets(String routeId) {
        return webClientBuilder.build()
                .patch()
                .uri(routeServiceUrl, routeId)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    log.error("Tickets on route with id = {} is over", routeId);
                    return Mono.error(new TicketAmountException(TICKET_AMOUNT_MESSAGE));
                })
                .bodyToMono(Integer.class);
    }
}
