package com.easybustickets.service;

import com.easybustickets.dto.TicketInfo;

public interface InfoService {

    TicketInfo getTicketInfo(Long ticketId);
}
