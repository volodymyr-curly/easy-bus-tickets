package com.easybustickets.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ticket {

    private Long id;
    private String personName;
    private String routeId;
    private String paymentId;
    private String status;
}
