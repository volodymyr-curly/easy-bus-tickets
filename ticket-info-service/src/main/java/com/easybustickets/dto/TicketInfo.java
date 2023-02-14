package com.easybustickets.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketInfo {

    private String departure;
    private String departureTime;
    private String destination;
    private BigDecimal price;
    private Integer availableTickets;
    private String paymentId;
    private String status;
}
