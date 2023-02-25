package com.easybustickets.ticket_buying_service.data;

import com.easybustickets.ticket_buying_service.dto.PaymentRequest;
import com.easybustickets.ticket_buying_service.dto.TicketRequest;
import com.easybustickets.ticket_buying_service.dto.TicketResponse;
import com.easybustickets.ticket_buying_service.model.Ticket;

import java.math.BigDecimal;

public class TicketControllerTestData {

    public static final String TICKETS_URL = "/api/tickets";
    public static final String ROUTES_URL = "/api/routes/";
    public static final String PAYMENT_URL = "/api/payment";
    public static final String GET_TICKET_URL = "/api/tickets/{id}";
    public static final String TICKET_ID = "ticket_id";
    public static final String ROUTE_ID = "1";

    public TicketRequest generateTicketRequest() {
        return new TicketRequest("name", ROUTE_ID, "100.00");
    }

    public TicketResponse generateTicketResponse() {
        return TicketResponse.builder()
                .id(TICKET_ID)
                .personName("name")
                .routeId("1")
                .paymentId("payment_id")
                .price("100.00")
                .seatNumber(1)
                .build();
    }

    public TicketResponse generateTicketResponseWhenFailed() {
        return TicketResponse.builder()
                .id(TICKET_ID)
                .personName("name")
                .routeId("1")
                .paymentId("payment_id")
                .price("100.00")
                .build();
    }

    public Ticket generateTicket() {
        return Ticket.builder()
                .id(TICKET_ID)
                .personName("name")
                .routeId("1")
                .paymentId("payment_id")
                .price(new BigDecimal("100.00"))
                .seatNumber(1)
                .build();
    }

    public PaymentRequest generatePaymentRequest() {
        return new PaymentRequest("name", new BigDecimal("100.00"));
    }
}
