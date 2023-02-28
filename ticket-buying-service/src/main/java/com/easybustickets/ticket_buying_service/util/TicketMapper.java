package com.easybustickets.ticket_buying_service.util;

import com.easybustickets.ticket_buying_service.dto.payment.PaymentResponse;
import com.easybustickets.ticket_buying_service.dto.route.RouteResponse;
import com.easybustickets.ticket_buying_service.dto.ticket.TicketInfoResponse;
import com.easybustickets.ticket_buying_service.dto.ticket.TicketRequest;
import com.easybustickets.ticket_buying_service.dto.ticket.TicketResponse;
import com.easybustickets.ticket_buying_service.model.Ticket;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static com.easybustickets.ticket_buying_service.model.PaymentStatus.DONE;

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

    public Mono<TicketInfoResponse> convertToTicketInfoResponse(Mono<Ticket> ticketMono,
                                                                Mono<RouteResponse> routeMono,
                                                                Mono<PaymentResponse> paymentMono) {
        return Mono.zip(ticketMono, routeMono, paymentMono)
                .map(tuple -> {
                    Ticket ticket = tuple.getT1();
                    RouteResponse route = tuple.getT2();
                    PaymentResponse payment = tuple.getT3();
                    TicketInfoResponse ticketInfo = new TicketInfoResponse();
                    modelMapper.map(route, ticketInfo);
                    modelMapper.map(ticket, ticketInfo);
                    modelMapper.map(payment, ticketInfo);
                    if (!payment.getStatus().equals(DONE.name())) {
                        ticketInfo.setAvailableTickets(route.getTicketsAmount());
                    }
                    return ticketInfo;
                });
    }
}
