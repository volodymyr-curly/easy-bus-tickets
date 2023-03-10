package com.easybustickets.ticket_service.dto.route;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RouteResponse {

    private Integer id;
    private String departure;
    private String departureTime;
    private String destination;
    private String destinationTime;
    private BigDecimal price;
    private Integer ticketsAmount;
}
