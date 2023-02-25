package com.easybustickets.ticket_buying_service.controller;

import com.easybustickets.ticket_buying_service.dto.TicketRequest;
import com.easybustickets.ticket_buying_service.dto.TicketResponse;
import com.easybustickets.ticket_buying_service.model.Ticket;
import com.easybustickets.ticket_buying_service.service.TicketService;
import com.easybustickets.ticket_buying_service.util.TicketMapper;
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

    @PostMapping()
    public Mono<TicketResponse> createTicket(@RequestBody TicketRequest ticketRequest) {
        log.debug("Create {}", ticketRequest);
        Mono<Ticket> ticketMono = ticketService.addTicket(ticketMapper.convertToTicket(ticketRequest));
        log.debug("Success when create {}", ticketMono);
        return ticketMapper.convertToTicketResponse(ticketMono);
    }

    @GetMapping("/{ticketId}")
    public Mono<TicketResponse> showTicket(@PathVariable("ticketId") String id) {
        log.debug("Show route");
        Mono<TicketResponse> ticketResponse = ticketMapper.convertToTicketResponse(ticketService.getTicket(id));
        log.debug("Success when show route");
        return ticketResponse;
    }
}
