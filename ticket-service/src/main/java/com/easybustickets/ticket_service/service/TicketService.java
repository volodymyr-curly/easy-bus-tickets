package com.easybustickets.ticket_service.service;

import com.easybustickets.ticket_service.model.Ticket;
import reactor.core.publisher.Mono;

public interface TicketService {

    Mono<Ticket> addTicket(Mono<Ticket> ticket);

    Mono<Ticket> getTicket(String id);
}
