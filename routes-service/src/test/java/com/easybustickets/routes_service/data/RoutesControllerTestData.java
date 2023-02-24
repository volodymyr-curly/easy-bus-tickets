package com.easybustickets.routes_service.data;

import com.easybustickets.routes_service.dto.RouteResponse;

import java.math.BigDecimal;

public class RoutesControllerTestData {

    public static final String SHOW_ALL_ROUTES_URL = "/api/routes";
    public static final String SHOW_ROUTE_URL = "/api/routes/{id}";
    public static final Integer ROUTE_ID = 1;
    public static final Integer EXPECTED_SEAT_NUMBER = 1;
    public static final int ELEMENT_INDEX = 0;

    public RouteResponse generateRouteResponse() {
        return RouteResponse.builder()
                .id(ROUTE_ID)
                .departure("Dnipro")
                .departureTime("28-02-2023 08:00")
                .destination("Kyiv")
                .destinationTime("28-02-2023 16:00")
                .price(new BigDecimal("100.00"))
                .seatsAmount(30)
                .ticketsAmount(30)
                .build();
    }

    public RouteResponse generateRouteRequest() {
        return RouteResponse.builder()
                .departure("Dnipro")
                .departureTime("28-02-2023 08:00")
                .destination("Kyiv")
                .destinationTime("28-02-2023 16:00")
                .price(new BigDecimal("100.00"))
                .seatsAmount(30)
                .ticketsAmount(30)
                .build();
    }

    public RouteResponse generateUpdatedRouteResponse() {
        return RouteResponse.builder()
                .id(ROUTE_ID)
                .departure("Dnipro")
                .departureTime("28-02-2023 09:00")
                .destination("Kyiv")
                .destinationTime("28-02-2023 17:00")
                .price(new BigDecimal("100.00"))
                .seatsAmount(30)
                .ticketsAmount(30)
                .build();
    }

    public RouteResponse generateUpdatedRouteRequest() {
        return RouteResponse.builder()
                .departure("Dnipro")
                .departureTime("28-02-2023 09:00")
                .destination("Kyiv")
                .destinationTime("28-02-2023 17:00")
                .price(new BigDecimal("100.00"))
                .seatsAmount(30)
                .ticketsAmount(30)
                .build();
    }
}
