package com.easybustickets.routes_service.repository;

import com.easybustickets.routes_service.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface RouteRepository extends JpaRepository<Route, Integer> {

    @Query("select case when count(r)> 0 then true else false end from Route r " +
            "where r.departure=?1 and r.departureTime=?2 and " +
            "r.destination=?3 and r.destinationTime=?4")
    Boolean exists(String departure, LocalDateTime departureTime,
                   String destination, LocalDateTime destinationTime);
}
