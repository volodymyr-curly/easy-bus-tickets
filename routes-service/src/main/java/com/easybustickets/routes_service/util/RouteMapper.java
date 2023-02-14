package com.easybustickets.routes_service.util;

import com.easybustickets.routes_service.dto.RouteRequest;
import com.easybustickets.routes_service.dto.RouteResponse;
import com.easybustickets.routes_service.model.Route;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RouteMapper {

    private final ModelMapper modelMapper;

    public RouteResponse convertToRouteResponse (Route route) {
        return modelMapper.map(route, RouteResponse.class);
    }

    public Route convertToRoute (RouteRequest routeRequest) {
        return modelMapper.map(routeRequest, Route.class);
    }
}
