package com.easybustickets.ticket_service.data;

import com.easybustickets.ticket_service.dto.payment.PaymentRequest;
import com.easybustickets.ticket_service.dto.ticket.TicketInfoResponse;
import com.easybustickets.ticket_service.dto.ticket.TicketRequest;
import com.easybustickets.ticket_service.dto.ticket.TicketResponse;
import com.easybustickets.ticket_service.model.Ticket;

import java.math.BigDecimal;

public class TicketControllerTestData {

    public static final String TICKETS_URL = "/api/tickets";
    public static final String ROUTES_URL = "/api/routes/";
    public static final String PAYMENT_URL = "/api/payment";
    public static final String GET_PAYMENT_URL = "/api/payment-status/";
    public static final String GET_TICKET_URL = "/api/tickets/{id}";
    public static final String TICKET_ID = "ticket_id";
    public static final String ROUTE_ID = "1";
    public static final String PAYMENT_ID = "payment_id";
    public static final String SEAT_NUMBER = "1";
    public static final int NOT_FOUND_STATUS = 404;
    public static final int BAD_REQUEST_STATUS = 400;
    public static final int SERVER_ERROR_STATUS = 500;


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

    public Ticket generateTicketWithFailedStatus() {
        return Ticket.builder()
                .id(TICKET_ID)
                .personName("name")
                .routeId("1")
                .paymentId("payment_id")
                .price(new BigDecimal("100.00"))
                .build();
    }

    public PaymentRequest generatePaymentRequest() {
        return new PaymentRequest("name", new BigDecimal("100.00"));
    }

    public TicketInfoResponse generateTicketInfoResponse() {
        return TicketInfoResponse.builder()
                .departure("Dnipro")
                .departureTime("28-02-2023 08:00")
                .destination("Kyiv")
                .destinationTime("28-02-2023 16:00")
                .price("100.00")
                .paymentStatus("DONE")
                .seatNumber(1)
                .build();
    }

    public TicketInfoResponse generateTicketInfoResponseWhenFailed() {
        return TicketInfoResponse.builder()
                .departure("Dnipro")
                .departureTime("28-02-2023 08:00")
                .destination("Kyiv")
                .destinationTime("28-02-2023 16:00")
                .price("100.00")
                .paymentStatus("FAILED")
                .availableTickets(30)
                .build();
    }
}
