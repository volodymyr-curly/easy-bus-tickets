package com.easybustickets.service;

import com.easybustickets.dto.Route;
import com.easybustickets.dto.Ticket;
import com.easybustickets.dto.TicketInfo;
import com.easybustickets.util.TicketInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class InfoServiceImpl implements InfoService {

    private final WebClient.Builder webClientBuilder;
    private final TicketInfoMapper infoMapper;

    @Value("${ticket-buying-service.base-url}")
    private String ticketServiceBaseUrl;

    @Value("${route-service.base-url}")
    private String routeServiceBaseUrl;

    public TicketInfo getTicketInfo(Long ticketId) {
        Ticket ticket = getTicket(ticketId);
        Route route = getRoute(ticket.getRouteId());
        return infoMapper.convertToInfo(ticket, route);
    }

    private Ticket getTicket(Long ticketId) {
        return webClientBuilder.build()
                .get().uri(ticketServiceBaseUrl + ticketId)
                .retrieve()
                .bodyToMono(Ticket.class)
                .block();
    }

    private Route getRoute(String routeId) {
        return webClientBuilder.build()
                .get().uri(routeServiceBaseUrl + routeId)
                .retrieve()
                .bodyToMono(Route.class)
                .block();
    }
}
