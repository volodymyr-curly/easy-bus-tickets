package com.easybustickets.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Route {

    private Long id;
    private String departure;
    private String destination;
    private String departureTime;
    private BigDecimal price;
    private Integer ticketsAmount;
}
