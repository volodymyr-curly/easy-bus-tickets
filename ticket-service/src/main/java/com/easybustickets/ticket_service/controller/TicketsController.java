package com.easybustickets.ticket_service.controller;

import com.easybustickets.ticket_service.client.PaymentClient;
import com.easybustickets.ticket_service.client.RouteClient;
import com.easybustickets.ticket_service.dto.ticket.TicketRequest;
import com.easybustickets.ticket_service.dto.ticket.TicketResponse;
import com.easybustickets.ticket_service.dto.ticket.TicketInfoResponse;
import com.easybustickets.ticket_service.model.Ticket;
import com.easybustickets.ticket_service.service.TicketService;
import com.easybustickets.ticket_service.util.TicketMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketsController {

    private final TicketService ticketService;
    public final TicketMapper ticketMapper;
    public final RouteClient routeClient;
    public final PaymentClient paymentClient;

    @PostMapping()
    public Mono<TicketResponse> createTicket(@RequestBody TicketRequest ticketRequest) {
        log.debug("Create {}", ticketRequest);
        Mono<Ticket> ticketMono = ticketService.addTicket(ticketMapper.convertToTicket(ticketRequest));
        log.debug("Success when create {}", ticketMono);
        return ticketMapper.convertToTicketResponse(ticketMono);
    }

    @GetMapping("/{ticketId}")
    public Mono<TicketInfoResponse> showTicket(@PathVariable("ticketId") String id) {
        log.debug("Show ticket info");
        Mono<Ticket> ticketMono = ticketService.getTicket(id);
        Mono<TicketInfoResponse> ticketInfo = ticketMapper
                .convertToTicketInfoResponse(
                        ticketMono,
                        ticketMono.flatMap(ticket -> routeClient.getRouteResponse(ticket.getRouteId())),
                        ticketMono.flatMap(ticket -> paymentClient.getPayment(ticket.getPaymentId()))
                );
        log.debug("Success when show ticket info");
        return ticketInfo;
    }
}
