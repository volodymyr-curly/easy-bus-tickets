package com.easybustickets.ticket_status_checker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {

    private Long id;
    private String personName;
    private String routeId;
    private String paymentId;
    private String status;
}
