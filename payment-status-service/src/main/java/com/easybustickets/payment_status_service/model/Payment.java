package com.easybustickets.payment_status_service.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="payment_status")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {

    @Id
    @Column(name = "status_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paymentId;
    private String status;
}
