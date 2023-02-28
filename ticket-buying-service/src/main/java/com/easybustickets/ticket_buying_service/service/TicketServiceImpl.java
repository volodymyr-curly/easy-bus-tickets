package com.easybustickets.ticket_buying_service.service;

import com.easybustickets.ticket_buying_service.client.PaymentClient;
import com.easybustickets.ticket_buying_service.client.RouteClient;
import com.easybustickets.ticket_buying_service.dto.payment.PaymentResponse;
import com.easybustickets.ticket_buying_service.exception.EntityNotFoundException;
import com.easybustickets.ticket_buying_service.exception.TicketAmountException;
import com.easybustickets.ticket_buying_service.model.Ticket;
import com.easybustickets.ticket_buying_service.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.easybustickets.ticket_buying_service.exception.ExceptionMessage.TICKET_AMOUNT_MESSAGE;
import static com.easybustickets.ticket_buying_service.exception.ExceptionMessage.TICKET_NOT_FOUND_MESSAGE;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final RouteClient routeClient;
    private final PaymentClient paymentClient;

    @Override
    public Mono<Ticket> addTicket(Mono<Ticket> ticket) {
        return ticket
                .flatMap(t -> paymentClient.createPayment(t.getPersonName(), t.getPrice())
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

    private Mono<Ticket> setSeatNumber(Ticket ticket, PaymentResponse paymentResponse) {
        return Mono.just(ticket)
                .filter(t -> paymentResponse.getStatus().equals("DONE"))
                .flatMap(t -> routeClient.decrementAvailableTickets(t.getRouteId())
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
}
