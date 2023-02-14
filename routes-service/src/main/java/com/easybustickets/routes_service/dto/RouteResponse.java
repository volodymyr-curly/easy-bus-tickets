package com.easybustickets.routes_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
