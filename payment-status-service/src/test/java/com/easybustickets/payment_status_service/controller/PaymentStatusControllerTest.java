package com.easybustickets.payment_status_service.controller;

import com.easybustickets.payment_status_service.dto.PaymentResponse;
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
import org.springframework.test.web.servlet.MvcResult;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Objects;

import static com.easybustickets.payment_status_service.exception.ExceptionMessage.PAYMENT_EXISTS_MESSAGE;
import static com.easybustickets.payment_status_service.exception.ExceptionMessage.PAYMENT_NOT_FOUND_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class PaymentStatusControllerTest {

    public static final String SAVE_PAYMENT_URL = "/api/payment-status/{paymentId}";
    public static final String PAYMENT_ID = "payment_id";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldReturn_PaymentResponse_WhenCreatePayment() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(SAVE_PAYMENT_URL, PAYMENT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        PaymentResponse actualPaymentResponse = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertEquals(PAYMENT_ID, actualPaymentResponse.getPaymentId());
        assertNotNull(actualPaymentResponse.getStatus());
    }

    @Test
    @Sql("classpath:/test_data.sql")
    void shouldReturn_EntityExistsException_WhenUpdatePayment() throws Exception {
        mockMvc.perform(post(SAVE_PAYMENT_URL, PAYMENT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityExistsException))
                .andExpect(result -> assertEquals(PAYMENT_EXISTS_MESSAGE,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }


    @Test
    @Sql("classpath:/test_data.sql")
    void shouldReturn_PaymentResponse_WhenUpdatePayment() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put(SAVE_PAYMENT_URL, PAYMENT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        PaymentResponse actualPaymentResponse = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertEquals(PAYMENT_ID, actualPaymentResponse.getPaymentId());
        assertNotNull(actualPaymentResponse.getStatus());
    }

    @Test
    void shouldReturn_EntityNotFoundException_WhenUpdatePayment() throws Exception {
        mockMvc.perform(put(SAVE_PAYMENT_URL, PAYMENT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals(PAYMENT_NOT_FOUND_MESSAGE,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }


}
