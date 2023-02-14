package com.easybustickets.util;

import com.easybustickets.dto.Route;
import com.easybustickets.dto.Ticket;
import com.easybustickets.dto.TicketInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketInfoMapper {

    public TicketInfo convertToInfo(Ticket ticket, Route route) {
        if (ticket.getStatus().equals("FAILED")) {
            return TicketInfo.builder()
                    .availableTickets(route.getTicketsAmount())
                    .status(ticket.getStatus())
                    .build();
        }
        return TicketInfo.builder()
                .departure(route.getDeparture())
                .departureTime(route.getDeparture())
                .destination(route.getDestination())
                .price(route.getPrice())
                .paymentId(ticket.getPaymentId())
                .status(ticket.getStatus())
                .build();
    }
}
