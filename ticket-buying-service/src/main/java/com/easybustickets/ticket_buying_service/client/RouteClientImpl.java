package com.easybustickets.ticket_buying_service.client;

import com.easybustickets.ticket_buying_service.dto.route.RouteResponse;
import com.easybustickets.ticket_buying_service.exception.EntityNotFoundException;
import com.easybustickets.ticket_buying_service.exception.TicketAmountException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.easybustickets.ticket_buying_service.exception.ExceptionMessage.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RouteClientImpl implements RouteClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${routes-service.url}")
    private String routeServiceUrl;

    @Override
    public Mono<RouteResponse> getRouteResponse(String routeId) {
        return webClientBuilder.build()
                .get()
                .uri(routeServiceUrl, routeId)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    log.error("Failed to get route with id = {}", routeId);
                    return Mono.error(new EntityNotFoundException(ROUTE_NOT_FOUND_MESSAGE));
                })
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    log.error("Internal Server Error");
                    return Mono.error(new RuntimeException(INTERNAL_SERVER_ERROR_MESSAGE));
                })
                .bodyToMono(RouteResponse.class);
    }

    @Override
    public Mono<Integer> decrementAvailableTickets(String routeId) {
        return webClientBuilder.build()
                .patch()
                .uri(routeServiceUrl, routeId)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    log.error("Tickets on route with id = {} is over", routeId);
                    return Mono.error(new TicketAmountException(TICKET_AMOUNT_MESSAGE));
                })
                .bodyToMono(Integer.class);
    }
}
