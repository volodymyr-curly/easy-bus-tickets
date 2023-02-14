package com.easybustickets.ticket_buying_service.util;

import com.easybustickets.ticket_buying_service.dto.TicketRequest;
import com.easybustickets.ticket_buying_service.dto.TicketResponse;
import com.easybustickets.ticket_buying_service.model.Ticket;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketMapper {

    private final ModelMapper modelMapper;

    public Ticket convertToTicket (TicketRequest ticketRequest) {
        return Ticket.builder()
                .personName(ticketRequest.getPersonName())
                .routeId(ticketRequest.getRouteId())
                .build();
    }

    public Ticket convertToTicketFromResponse (TicketResponse ticketResponse) {
        return modelMapper.map(ticketResponse, Ticket.class);
    }

    public TicketResponse convertToTicketResponse(Ticket ticket) {
        return modelMapper.map(ticket, TicketResponse.class);
    }
}
