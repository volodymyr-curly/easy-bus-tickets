package com.easybustickets.ticket_buying_service.controller;

import com.easybustickets.ticket_buying_service.dto.TicketRequest;
import com.easybustickets.ticket_buying_service.dto.TicketResponse;
import com.easybustickets.ticket_buying_service.model.Ticket;
import com.easybustickets.ticket_buying_service.service.TicketService;
import com.easybustickets.ticket_buying_service.util.TicketMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketsController {

    private final TicketService ticketService;
    public final TicketMapper ticketMapper;

    @PostMapping()
    public Long createTicket(@RequestBody TicketRequest ticketRequest) {
        log.debug("Create {}", ticketRequest);
        Ticket ticket = ticketService.addTicket(ticketMapper.convertToTicket(ticketRequest));
        log.debug("Success when create {}", ticket);
        return ticket.getId();
    }

    @GetMapping("/{ticketId}")
    public TicketResponse showTicket(@PathVariable("ticketId") Long id) {
        log.debug("Show route");
        TicketResponse routeDTO = ticketMapper.convertToTicketResponse(ticketService.getTicket(id));
        log.debug("Success when show route");
        return routeDTO;
    }

    @GetMapping("/status/{status}")
    public List<TicketResponse> showTickets(@PathVariable String status) {
        return ticketService.getTicketByStatus(status).stream()
                .map(ticketMapper::convertToTicketResponse).toList();
    }

    @PatchMapping("/edit/status")
    public String updateTicketStatus(@RequestBody TicketResponse ticketDTO) {
        return ticketService.updateTicketStatus(ticketMapper.convertToTicketFromResponse(ticketDTO));
    }
}
