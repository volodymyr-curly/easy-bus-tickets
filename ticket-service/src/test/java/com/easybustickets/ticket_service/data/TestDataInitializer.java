package com.easybustickets.ticket_service.data;

import com.easybustickets.ticket_service.model.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestDataInitializer {

    private final ReactiveMongoTemplate mongoTemplate;

    public void insertTicket(Ticket ticket) {
        mongoTemplate.insert(ticket).block();
    }
}
