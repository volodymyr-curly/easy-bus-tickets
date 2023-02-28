package com.easybustickets.ticket_service.dto.ticket;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@EqualsAndHashCode(exclude = {"id"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketResponse {

    private String id;
    private String personName;
    private String routeId;
    private String paymentId;
    private String price;
    private Integer seatNumber;
}
