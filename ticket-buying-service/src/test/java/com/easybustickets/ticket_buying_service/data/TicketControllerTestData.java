package com.easybustickets.ticket_buying_service.data;

import com.easybustickets.ticket_buying_service.dto.TicketRequest;
import com.easybustickets.ticket_buying_service.dto.TicketResponse;
import com.easybustickets.ticket_buying_service.model.Ticket;

public class TicketControllerTestData {

    public static final String TICKET_URL = "/api/tickets";
    public static final String GET_TICKET_URL = "/api/tickets/{ticketId}";
    public static final String GET_NEW_TICKETS_URL = "/api/tickets//status/{status}";
    public static final Long TICKET_ID = 1L;
    public static final int TICKET_LIST_SIZE = 1;

    public TicketRequest generateTicketDTO() {
        return TicketRequest.builder()
                .personName("Name")
                .routeId("1")
                .build();
    }

    public TicketResponse generateTicketResponse() {
        return TicketResponse.builder()
                .id(TICKET_ID)
                .personName("Name")
                .routeId("1")
                .paymentId("payment_id")
                .status("NEW")
                .build();
    }

    public Ticket generateTicket() {
        return Ticket.builder()
                .id(TICKET_ID)
                .personName("Name")
                .routeId("1")
                .paymentId("payment_id")
                .status("NEW")
                .build();
    }
}
