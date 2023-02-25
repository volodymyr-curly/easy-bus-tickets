package com.easybustickets.ticket_buying_service.util;

import com.easybustickets.ticket_buying_service.dto.TicketRequest;
import com.easybustickets.ticket_buying_service.dto.TicketResponse;
import com.easybustickets.ticket_buying_service.model.Ticket;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class TicketMapper {

    private final ModelMapper modelMapper;

    public Mono<Ticket> convertToTicket (TicketRequest ticketRequest) {
        return Mono.just(Ticket.builder()
                .personName(ticketRequest.getPersonName())
                .routeId(ticketRequest.getRouteId())
                .price(new BigDecimal(ticketRequest.getPrice()))
                .build());
    }

    public Mono<TicketResponse> convertToTicketResponse(Mono<Ticket> ticketMono) {
        return ticketMono.map(ticket -> modelMapper.map(ticket, TicketResponse.class));
    }
}
