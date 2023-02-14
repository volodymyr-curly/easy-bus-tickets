package com.easybustickets.ticket_buying_service.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="tickets")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ticket {

    @Id
    @Column(name = "ticket_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String personName;
    private String routeId;
    private String paymentId;
    private String status;
}
