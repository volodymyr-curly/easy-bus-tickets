package com.easybustickets.ticket_buying_service.controller;

import com.easybustickets.ticket_buying_service.data.TicketControllerTestData;
import com.easybustickets.ticket_buying_service.dto.TicketResponse;
import com.easybustickets.ticket_buying_service.model.PaymentStatus;
import com.easybustickets.ticket_buying_service.model.Ticket;
import com.easybustickets.ticket_buying_service.service.TicketService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;

import static com.easybustickets.ticket_buying_service.exception.ExceptionMessage.TICKET_NOT_FOUND_MESSAGE;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class TicketControllerTest extends TicketControllerTestData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private TicketService ticketService;

    @Test
    @Sql("classpath:/test_data.sql")
    void shouldReturn_Ticket_WhenShowTicket() throws Exception {
        mockMvc.perform(get(GET_TICKET_URL, TICKET_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personName", is(generateTicketDTO().getPersonName())))
                .andExpect(jsonPath("$.routeId", is(generateTicketDTO().getRouteId())));

    }

    @Test
    void shouldThrow_EntityNotFoundException_WhenShowTicket() throws Exception {
        mockMvc.perform(get(GET_TICKET_URL, TICKET_ID))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals(TICKET_NOT_FOUND_MESSAGE,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @Sql("classpath:/test_data.sql")
    void shouldReturn_TicketsList_WhenShowNewTickets() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(GET_NEW_TICKETS_URL, PaymentStatus.NEW.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(TICKET_LIST_SIZE)))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        TicketResponse actualTicketResponse = objectMapper.readValue(content, new TypeReference<List<TicketResponse>>() {
        }).get(0);
        TicketResponse expectedTicketResponse = generateTicketResponse();
        assertEquals(expectedTicketResponse, actualTicketResponse);
    }


}
