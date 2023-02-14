package com.easybustickets.ticket_buying_service.service;

import com.easybustickets.ticket_buying_service.model.Ticket;

import java.util.List;

public interface TicketService {

    Ticket addTicket(Ticket ticket);

    Ticket getTicket(Long id);

    List<Ticket> getTicketByStatus(String status);

    String updateTicketStatus(Ticket ticket);
}
