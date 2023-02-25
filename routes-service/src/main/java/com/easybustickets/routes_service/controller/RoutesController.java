package com.easybustickets.routes_service.controller;

import com.easybustickets.routes_service.dto.RouteRequest;
import com.easybustickets.routes_service.dto.RouteResponse;
import com.easybustickets.routes_service.model.Route;
import com.easybustickets.routes_service.service.RouteService;
import com.easybustickets.routes_service.util.RouteMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
public class RoutesController {

    private final RouteService routeService;
    private final RouteMapper routeMapper;

    @GetMapping
    public List<RouteResponse> showRoutes() {
        log.debug("Show routes");
        List<RouteResponse> routesResponse = routeService.getAllRoutes().stream()
                .map(routeMapper::convertToRouteResponse).toList();
        log.debug("Success when show routes");
        return routesResponse;
    }

    @GetMapping("/{id}")
    public RouteResponse showRoute(@PathVariable Integer id) {
        log.debug("Show route");
        RouteResponse routeResponse = routeMapper.convertToRouteResponse(routeService.getRout(id));
        log.debug("Success when show route");
        return routeResponse;
    }

    @PatchMapping("/{id}")
    public Integer updateRouteTickets(@PathVariable Integer id) {
        log.debug("Update tickets amount of route with id={}", id);
        Integer seatNumber = routeService.changeTicketsAmount(id);
        log.debug("Seat number = {}", seatNumber);
        return seatNumber;
    }

    @PostMapping()
    public RouteResponse createRoute(@RequestBody RouteRequest routeRequest) {
        log.debug("Create {}", routeRequest);
        Route route = routeService.addRoute(routeMapper.convertToRoute(routeRequest));
        log.debug("Success when create {}", route);
        return routeMapper.convertToRouteResponse(route);
    }

    @PutMapping("/{id}")
    public RouteResponse editRoute(@RequestBody RouteRequest routeRequest,
                                   @PathVariable Integer id) {
        log.debug("Update route with id={}", id);
        Route route = routeMapper.convertToRoute(routeRequest);
        Route updatedRoute = routeService.updateRoute(id, route);
        log.debug("Success when update {}", updatedRoute);
        return routeMapper.convertToRouteResponse(updatedRoute);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> removeRoute(@PathVariable Integer id) {
        log.debug("Remove route with id={}", id);
        routeService.deleteRoute(id);
        log.debug("Success when remove route with id={}", id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
