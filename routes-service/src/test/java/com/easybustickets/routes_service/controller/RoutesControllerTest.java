package com.easybustickets.routes_service.controller;

import com.easybustickets.routes_service.data.RoutesControllerTestData;
import com.easybustickets.routes_service.dto.RouteResponse;
import com.easybustickets.routes_service.exception.TicketAmountException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;

import static com.easybustickets.routes_service.exception.ExceptionMessage.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class RoutesControllerTest extends RoutesControllerTestData {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @Sql("classpath:/db/migration/V2__Add_Data.sql")
    void shouldReturn_ListOfRoutes_WhenShowAllRoutes() throws Exception {
        mockMvc.perform(get(SHOW_ALL_ROUTES_URL))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    RouteResponse actualRoute = objectMapper.readValue(content,
                            new TypeReference<List<RouteResponse>>() {
                            }).get(ELEMENT_INDEX);
                    RouteResponse expectedRoute = generateRouteResponse();
                    assertEquals(expectedRoute, actualRoute);
                });
    }

    @Test
    void shouldThrow_EntityNotFoundException_WhenShowAllRoutes() throws Exception {
        mockMvc.perform(get(SHOW_ALL_ROUTES_URL))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals(ROUTES_NOT_FOUND_MESSAGE,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @Sql("classpath:/db/migration/V2__Add_Data.sql")
    void shouldReturn_Route_WhenShowRoute() throws Exception {
        mockMvc.perform(get(SHOW_ROUTE_URL, ROUTE_ID))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    RouteResponse actualRoute = objectMapper.readValue(content, new TypeReference<>() {
                    });
                    RouteResponse expectedRoute = generateRouteResponse();
                    assertEquals(expectedRoute, actualRoute);
                    assertEquals(ROUTE_ID, actualRoute.getId());
                });
    }

    @Test
    void shouldThrow_EntityNotFoundException_WhenShowRoute() throws Exception {
        mockMvc.perform(get(SHOW_ROUTE_URL, ROUTE_ID))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals(ROUTE_NOT_FOUND_MESSAGE,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void shouldReturn_Route_WhenCreateRoute() throws Exception {
        mockMvc.perform(post(SHOW_ALL_ROUTES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(generateRouteRequest())))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    RouteResponse actualRoute = objectMapper.readValue(content, new TypeReference<>() {
                    });
                    RouteResponse expectedRoute = generateRouteResponse();
                    assertEquals(expectedRoute, actualRoute);
                    assertEquals(ROUTE_ID, actualRoute.getId());
                });
    }

    @Test
    @Sql("classpath:/db/migration/V2__Add_Data.sql")
    void shouldThrow_EntityExistsException_WhenCreateRoute() throws Exception {
        mockMvc.perform(post(SHOW_ALL_ROUTES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(generateRouteRequest())))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityExistsException))
                .andExpect(result -> assertEquals(ROUTE_EXISTS_MESSAGE,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @Sql("classpath:/db/migration/V2__Add_Data.sql")
    void shouldReturn_Route_WhenUpdateRoute() throws Exception {
        mockMvc.perform(put(SHOW_ROUTE_URL, ROUTE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(generateUpdatedRouteRequest())))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    RouteResponse actualRoute = objectMapper.readValue(content, new TypeReference<>() {
                    });
                    RouteResponse expectedRoute = generateUpdatedRouteResponse();
                    assertEquals(expectedRoute, actualRoute);
                });
    }

    @Test
    @Sql("classpath:/db/migration/V2__Add_Data.sql")
    void shouldThrow_EntityExistsException_WhenUpdateRoute() throws Exception {
        mockMvc.perform(put(SHOW_ROUTE_URL, ROUTE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(generateRouteRequest())))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityExistsException))
                .andExpect(result -> assertEquals(ROUTE_EXISTS_MESSAGE,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void shouldThrow_EntityNotFoundException_WhenUpdateRoute() throws Exception {
        mockMvc.perform(put(SHOW_ROUTE_URL, ROUTE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(generateUpdatedRouteRequest())))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals(ROUTE_NOT_FOUND_MESSAGE,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @Sql("classpath:/db/migration/V2__Add_Data.sql")
    void shouldReturn_PositiveStatus_WhenRemoveRoute() throws Exception {
        mockMvc.perform(delete(SHOW_ROUTE_URL, ROUTE_ID))
                .andExpect(status().isOk());
    }

    @Test
    void shouldThrow_EntityNotFoundException_WhenRemoveRoute() throws Exception {
        mockMvc.perform(delete(SHOW_ROUTE_URL, ROUTE_ID))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals(ROUTE_NOT_FOUND_MESSAGE,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @Sql("classpath:/db/migration/V2__Add_Data.sql")
    void shouldReturn_Route_WhenChangeTicketsAmount() throws Exception {
        mockMvc.perform(patch(SHOW_ROUTE_URL, ROUTE_ID))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    RouteResponse actualRoute = objectMapper.readValue(content, new TypeReference<>() {
                    });
                    RouteResponse expectedRoute = generateRouteResponse();
                    expectedRoute.setTicketsAmount(EXPECTED_TICKETS_AMOUNT);
                    assertEquals(expectedRoute, actualRoute);
                });
    }

    @Test
    void shouldThrow_EntityNotFoundException_WhenChangeTicketsAmount() throws Exception {
        mockMvc.perform(patch(SHOW_ROUTE_URL, ROUTE_ID))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals(ROUTE_NOT_FOUND_MESSAGE,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @Sql("classpath:/test_data.sql")
    void shouldThrow_TicketAmountException_WhenChangeTicketsAmount() throws Exception {
        mockMvc.perform(patch(SHOW_ROUTE_URL, ROUTE_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TicketAmountException))
                .andExpect(result -> assertEquals(TICKET_AMOUNT_MESSAGE,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }
}
