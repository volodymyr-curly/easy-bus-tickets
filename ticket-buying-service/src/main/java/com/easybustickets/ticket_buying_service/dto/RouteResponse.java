package com.easybustickets.ticket_buying_service.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RouteResponse {

    private Long id;
    private String departure;
    private String destination;
    private String departureTime;
    private BigDecimal price;
    private Integer ticketsAmount;
}
