package com.easybustickets.payment_service.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {

    String personName;
    BigDecimal ticketPrice;
}
