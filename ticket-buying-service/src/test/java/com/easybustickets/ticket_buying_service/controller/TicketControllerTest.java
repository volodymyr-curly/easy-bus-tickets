package com.easybustickets.ticket_buying_service.controller;

import com.easybustickets.ticket_buying_service.data.TestDataInitializer;
import com.easybustickets.ticket_buying_service.data.TicketControllerTestData;
import com.easybustickets.ticket_buying_service.dto.TicketResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@AutoConfigureWireMock(port = 8086)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class TicketControllerTest extends TicketControllerTestData {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestDataInitializer testDataInitializer;

    @Test
    void shouldReturn_TicketResponse_WhenCreateTicketWith() throws Exception {
        stubFor(post(urlEqualTo(PAYMENT_URL))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(generatePaymentRequest())))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("done_payment_response.json")));

        stubFor(patch(urlEqualTo(ROUTES_URL + ROUTE_ID))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("1")));

        webTestClient
                .post()
                .uri(TICKETS_URL)
                .bodyValue(generateTicketRequest())
                .exchange()
                .expectStatus().isOk()
                .expectBody(TicketResponse.class)
                .consumeWith(ticketResponseEntityExchangeResult -> {
                    TicketResponse actualTicket = ticketResponseEntityExchangeResult.getResponseBody();
                    TicketResponse expectedTicket = generateTicketResponse();
                    assertEquals(expectedTicket, actualTicket);
                });
    }

    @Test
    void shouldReturn_TicketResponse_WhenCreateTicketWithFailedPaymentStatus() throws Exception {
        stubFor(post(urlEqualTo(PAYMENT_URL))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(generatePaymentRequest())))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("failed_payment_response.json")));

        webTestClient
                .post()
                .uri(TICKETS_URL)
                .bodyValue(generateTicketRequest())
                .exchange()
                .expectStatus().isOk()
                .expectBody(TicketResponse.class)
                .consumeWith(ticketResponseEntityExchangeResult -> {
                    TicketResponse actualTicket = ticketResponseEntityExchangeResult.getResponseBody();
                    TicketResponse expectedTicket = generateTicketResponseWhenFailed();
                    assertEquals(expectedTicket, actualTicket);
                });
    }

    @Test
    void shouldReturn_TicketsAmountMessage_WhenCreateTicket() throws JsonProcessingException {
        stubFor(post(urlEqualTo(PAYMENT_URL))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(generatePaymentRequest())))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("done_payment_response.json")));

        stubFor(patch(urlEqualTo(ROUTES_URL + ROUTE_ID))
                .willReturn(aResponse()
                        .withStatus(400)));

        webTestClient
                .post()
                .uri(TICKETS_URL)
                .bodyValue(generateTicketRequest())
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .isEqualTo("Tickets on this route is over");
    }

    @Test
    void shouldReturn_PaymentServiceServerError_WhenCreateTicket() throws Exception {
        stubFor(post(urlEqualTo(PAYMENT_URL))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(generatePaymentRequest())))
                .willReturn(aResponse()
                        .withStatus(500)));

        webTestClient
                .post()
                .uri(TICKETS_URL)
                .bodyValue(generateTicketRequest())
                .exchange()
                .expectStatus()
                .is5xxServerError()
                .expectBody(String.class)
                .isEqualTo("Internal server error");
    }

    @Test
    void shouldReturn_TicketResponse_WhenShowTicket() {
        testDataInitializer.insertTicket(generateTicket());

        webTestClient
                .get()
                .uri(GET_TICKET_URL, TICKET_ID)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(TicketResponse.class)
                .consumeWith(ticketResponseEntityExchangeResult -> {
                    TicketResponse actualTicket = ticketResponseEntityExchangeResult.getResponseBody();
                    TicketResponse expectedTicket = generateTicketResponse();
                    assertEquals(expectedTicket, actualTicket);
                });
    }

    @Test
    void shouldReturn_TicketNotFound_WhenShowTicket() {
        webTestClient
                .get()
                .uri(GET_TICKET_URL, TICKET_ID)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(String.class)
                .isEqualTo("Ticket not found");
    }
}
