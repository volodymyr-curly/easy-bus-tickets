package com.easybustickets.ticket_service.client;

import com.easybustickets.ticket_service.dto.route.RouteResponse;
import reactor.core.publisher.Mono;

public interface RouteClient {

    Mono<RouteResponse> getRouteResponse(String routeId);

    Mono<Integer> decrementAvailableTickets(String routeId);
}
