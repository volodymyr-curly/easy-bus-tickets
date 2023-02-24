package com.easybustickets.routes_service.service;

import com.easybustickets.routes_service.exception.TicketAmountException;
import com.easybustickets.routes_service.model.Route;
import com.easybustickets.routes_service.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;

import static com.easybustickets.routes_service.exception.ExceptionMessage.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {

    private final RouteRepository routeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Route> getAllRoutes() {
        List<Route> routes = routeRepository.findAll();

        if (routes.isEmpty()) {
            log.error("Failed to get routes");
            throw new EntityNotFoundException(ROUTES_NOT_FOUND_MESSAGE);
        }

        return routes;
    }

    @Override
    @Transactional(readOnly = true)
    public Route getRout(Integer id) {
        return getRouteById(id);
    }

    @Override
    public Integer changeTicketsAmount(Integer id) {
        Route routeToUpdate = getRouteById(id);
        int newTicketAmount = routeToUpdate.getTicketsAmount() - 1;

        if (newTicketAmount <= 0) {
            log.error("Tickets on route number {} is over", id);
            throw new TicketAmountException(TICKET_AMOUNT_MESSAGE);
        }
        routeToUpdate.setTicketsAmount(newTicketAmount);
        Route updatedRoute = routeRepository.save(routeToUpdate);
        return updatedRoute.getSeatNumber();
    }

    @Override
    public Route addRoute(Route route) {
        checkIfExist(route);
        return routeRepository.save(route);
    }

    @Override
    public Route updateRoute(Integer id, Route updatedRoute) {
        checkIfExist(updatedRoute);
        Route routeToUpdate = getRouteById(id);
        routeToUpdate.updateByRoute(updatedRoute);
        return routeRepository.save(routeToUpdate);
    }

    @Override
    public void deleteRoute(Integer id) {
        checkIfNotExist(id);
        routeRepository.deleteById(id);
    }


    private Route getRouteById(Integer id) {
        Route route = routeRepository.findById(id).orElseThrow(() -> {
            log.error("Failed to get route with id={}", id);
            return new EntityNotFoundException(ROUTE_NOT_FOUND_MESSAGE);
        });

        if (route.getTicketsAmount() <= 0) {
            log.error("Tickets on route number {} is over", id);
            throw new TicketAmountException(TICKET_AMOUNT_MESSAGE);
        }
        return route;
    }

    private void checkIfExist(Route route) {
        if (routeRepository.exists(route.getDeparture(), route.getDepartureTime(),
                route.getDestination(), route.getDestinationTime())) {
            log.error("Already exists {}", route);
            throw new EntityExistsException(ROUTE_EXISTS_MESSAGE);
        }
    }

    private void checkIfNotExist(Integer id) {
        if (!routeRepository.existsById(id)) {
            log.error("Route with id={} not found", id);
            throw new EntityNotFoundException(ROUTE_NOT_FOUND_MESSAGE);
        }
    }
}
