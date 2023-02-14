package com.easybustickets.controller;

import com.easybustickets.dto.TicketInfo;
import com.easybustickets.service.InfoServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/ticket-info")
@RequiredArgsConstructor
public class InfoController {

    private final InfoServiceImpl infoService;

    @GetMapping("/{ticketId}")
    public TicketInfo showTicketInfoByTicketId(@PathVariable Long ticketId) {
        log.debug("Generate ticket info by ticket id={}", ticketId);
        TicketInfo ticketInfo = infoService.getTicketInfo(ticketId);
        log.debug("Show {}", ticketInfo);
        return ticketInfo;
    }
}
