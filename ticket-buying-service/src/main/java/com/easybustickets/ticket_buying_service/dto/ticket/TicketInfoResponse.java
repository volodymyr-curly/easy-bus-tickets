package com.easybustickets.ticket_buying_service.dto.ticket;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketInfoResponse {

    private String departure;
    private String departureTime;
    private String destination;
    private String destinationTime;
    private String price;
    private String paymentStatus;
    private Integer seatNumber;
    private Integer availableTickets;

}
