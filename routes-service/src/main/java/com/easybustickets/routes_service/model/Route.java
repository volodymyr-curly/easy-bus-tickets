package com.easybustickets.routes_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="routes")
@Data
@EqualsAndHashCode(exclude = {"id", "price", "ticketsAmount"})
@AllArgsConstructor
@NoArgsConstructor
public class Route {

    @Id
    @Column(name = "route_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String departure;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime departureTime;

    private String destination;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime destinationTime;

    private BigDecimal price;
    private Integer seatsAmount;
    private Integer ticketsAmount;

    public void updateByRoute(Route updatedRoute) {
        this.setDeparture(updatedRoute.getDeparture());
        this.setDepartureTime(updatedRoute.getDepartureTime());
        this.setDestination(updatedRoute.getDestination());
        this.setDestinationTime(updatedRoute.getDestinationTime());
        this.setPrice(updatedRoute.getPrice());
        this.setTicketsAmount(updatedRoute.getSeatsAmount());
        this.setTicketsAmount(updatedRoute.getTicketsAmount());
    }

    public Integer getSeatNumber() {
        return this.seatsAmount-this.ticketsAmount;
    }
}
