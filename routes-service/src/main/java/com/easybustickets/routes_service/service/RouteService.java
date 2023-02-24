package com.easybustickets.routes_service.service;

import com.easybustickets.routes_service.model.Route;

import java.util.List;

public interface RouteService {

    List<Route> getAllRoutes();

    Route getRout(Integer id);

    Integer changeTicketsAmount(Integer id);

    Route addRoute (Route route);

    Route updateRoute (Integer id, Route route);

    void deleteRoute(Integer id);
}
