package com.easybustickets.ticket_buying_service.repository;

import com.easybustickets.ticket_buying_service.model.Ticket;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends ReactiveMongoRepository<Ticket, String> {
}
