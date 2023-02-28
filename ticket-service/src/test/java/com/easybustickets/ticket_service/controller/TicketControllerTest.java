package com.easybustickets.ticket_service.controller;

import com.easybustickets.ticket_service.data.TestDataInitializer;
import com.easybustickets.ticket_service.data.TicketControllerTestData;
import com.easybustickets.ticket_service.dto.ticket.TicketInfoResponse;
import com.easybustickets.ticket_service.dto.ticket.TicketResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.easybustickets.ticket_service.exception.ExceptionMessage.*;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@AutoConfigureWireMock(port = 8086)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@TestPropertySource(
        properties = {
                "routes-service.url=http://localhost:8086/api/routes/{id}",
                "payment-service.url=http://localhost:8086/api/payment",
                "payment-status-service.url=http://localhost:8086/api/payment-status/{id}"
        }
)
public class TicketControllerTest extends TicketControllerTestData {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestDataInitializer testDataInitializer;

    @Test
    void shouldReturn_TicketResponse_WhenCreateTicketWith() throws Exception {
        createPostStub("done_payment_response.json");
        createPatchStub();

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
        createPostStub("failed_payment_response.json");

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
    void shouldReturn_TicketsAmountMessage_WhenCreateTicket() throws Exception {
        createPostStub("done_payment_response.json");

        stubFor(patch(urlEqualTo(ROUTES_URL + ROUTE_ID))
                .willReturn(aResponse()
                        .withStatus(BAD_REQUEST_STATUS)));

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
                        .withStatus(SERVER_ERROR_STATUS)));

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
    void shouldReturn_TicketInfoResponse_WhenShowTicket() {
        testDataInitializer.insertTicket(generateTicket());
        createGetStub(ROUTES_URL + ROUTE_ID, "route_response.json");
        createGetStub(GET_PAYMENT_URL + PAYMENT_ID, "done_payment_response.json");

        webTestClient
                .get()
                .uri(GET_TICKET_URL, TICKET_ID)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(TicketInfoResponse.class)
                .consumeWith(ticketResponseEntityExchangeResult -> {
                    TicketInfoResponse actualTicket = ticketResponseEntityExchangeResult.getResponseBody();
                    TicketInfoResponse expectedTicket = generateTicketInfoResponse();
                    assertEquals(expectedTicket, actualTicket);
                });
    }

    @Test
    void shouldReturn_TicketInfoResponse_WhenShowTicketWithFailedStatus() {
        testDataInitializer.insertTicket(generateTicketWithFailedStatus());
        createGetStub(ROUTES_URL + ROUTE_ID, "route_response.json");
        createGetStub(GET_PAYMENT_URL + PAYMENT_ID, "failed_payment_response.json");

        webTestClient
                .get()
                .uri(GET_TICKET_URL, TICKET_ID)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(TicketInfoResponse.class)
                .consumeWith(ticketResponseEntityExchangeResult -> {
                    TicketInfoResponse actualTicket = ticketResponseEntityExchangeResult.getResponseBody();
                    TicketInfoResponse expectedTicket = generateTicketInfoResponseWhenFailed();
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
                .isEqualTo(TICKET_NOT_FOUND_MESSAGE);
    }

    @Test
    void shouldReturn_RouteNotFound_WhenShowTicket() {
        testDataInitializer.insertTicket(generateTicketWithFailedStatus());
        createGetStub(GET_PAYMENT_URL + PAYMENT_ID, "failed_payment_response.json");
        createGetErrorStub(ROUTES_URL + ROUTE_ID);

        webTestClient
                .get()
                .uri(GET_TICKET_URL, TICKET_ID)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(String.class)
                .isEqualTo(ROUTE_NOT_FOUND_MESSAGE);
    }

    @Test
    void shouldReturn_PaymentNotFound_WhenShowTicket() {
        testDataInitializer.insertTicket(generateTicketWithFailedStatus());
        createGetStub(ROUTES_URL + ROUTE_ID, "route_response.json");
        createGetErrorStub(GET_PAYMENT_URL + PAYMENT_ID);

        webTestClient
                .get()
                .uri(GET_TICKET_URL, TICKET_ID)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(String.class)
                .isEqualTo(PAYMENT_NOT_FOUND_MESSAGE);
    }

    private void createPostStub(String response) throws Exception {
        stubFor(post(urlEqualTo(PAYMENT_URL))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(generatePaymentRequest())))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile(response)));
    }

    private void createPatchStub() {
        stubFor(patch(urlEqualTo(ROUTES_URL + ROUTE_ID))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(SEAT_NUMBER)));
    }

    private void createGetStub(String url, String response) {
        stubFor(get(urlEqualTo(url))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile(response)));
    }

    private void createGetErrorStub(String url) {
        stubFor(get(urlEqualTo(url))
                .willReturn(aResponse()
                        .withStatus(NOT_FOUND_STATUS)));
    }
}
