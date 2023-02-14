package com.easybustickets.ticket_buying_service.service;

import com.easybustickets.ticket_buying_service.dto.PaymentRequest;
import com.easybustickets.ticket_buying_service.dto.PaymentResponse;
import com.easybustickets.ticket_buying_service.dto.RouteResponse;
import com.easybustickets.ticket_buying_service.exception.TicketAmountException;
import com.easybustickets.ticket_buying_service.model.Ticket;
import com.easybustickets.ticket_buying_service.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static com.easybustickets.ticket_buying_service.exception.ExceptionMessage.TICKET_AMOUNT_MESSAGE;
import static com.easybustickets.ticket_buying_service.exception.ExceptionMessage.TICKET_NOT_FOUND_MESSAGE;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final WebClient.Builder webClientBuilder;

    @Value("${route-service.base-url}")
    private String routeServiceBaseUrl;

    @Value("${payment-service.url}")
    private String paymentServiceUrl;

    @Value("${payment-status-service.base-url}")
    private String paymentStatusServiceBaseUrl;

    @Override
    @Transactional
    public Ticket addTicket(Ticket ticket) {
        RouteResponse route = getRouteResponse(ticket.getRouteId());

        if (route.getTicketsAmount() > 0) {
            PaymentResponse paymentResponse = createPayment(ticket.getPersonName(), route.getPrice());
            ticket.setPaymentId(paymentResponse.getPaymentId());
            ticket.setStatus(paymentResponse.getStatus());

            if (ticket.getStatus().equals("DONE")) {
                decrementAvailableTickets(ticket.getRouteId());
            }

            return ticketRepository.save(ticket);

        } else {
            throw new TicketAmountException(TICKET_AMOUNT_MESSAGE);
        }
    }

    @Override
    public Ticket getTicket(Long id) {
        return ticketRepository.findById(id).orElseThrow(() -> {
            log.error("Failed to get ticket with id={}", id);
            return new EntityNotFoundException(TICKET_NOT_FOUND_MESSAGE);
        });
    }

    @Override
    public List<Ticket> getTicketByStatus(String status) {
        return ticketRepository.findTicketsByStatus(status);
    }

    @Override
    @Transactional
    public String updateTicketStatus(Ticket ticket) {
        PaymentResponse paymentResponse = updatePayment(ticket.getPaymentId());
        Ticket updatedTicket = Ticket.builder()
                .id(ticket.getId())
                .personName(ticket.getPersonName())
                .routeId(ticket.getRouteId())
                .paymentId(paymentResponse.getPaymentId())
                .status(paymentResponse.getStatus())
                .build();
        ticketRepository.save(updatedTicket);
        return updatedTicket.getStatus();
    }

    private RouteResponse getRouteResponse(String routeId) {
        return Objects.requireNonNull(webClientBuilder.build()
                .get().uri(routeServiceBaseUrl + routeId)
                .retrieve()
                .bodyToMono(RouteResponse.class)
                .block());
    }

    private PaymentResponse createPayment(String personName, BigDecimal price) {
        return webClientBuilder.build()
                .post().uri(paymentServiceUrl)
                .bodyValue(PaymentRequest.builder()
                        .personName(personName)
                        .ticketPrice(price)
                        .build())
                .retrieve()
                .bodyToMono(PaymentResponse.class)
                .block();
    }

    private PaymentResponse updatePayment(String paymentId) {
        return webClientBuilder.build()
                .put()
                .uri(paymentStatusServiceBaseUrl + paymentId)
                .retrieve()
                .bodyToMono(PaymentResponse.class)
                .block();
    }

    private void decrementAvailableTickets(String routeId) {
        webClientBuilder.build()
                .patch().uri(routeServiceBaseUrl + routeId)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
