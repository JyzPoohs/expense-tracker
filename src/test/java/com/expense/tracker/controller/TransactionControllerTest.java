package com.expense.tracker.controller;

import com.expense.tracker.constant.ErrorCode;
import com.expense.tracker.dto.TransactionDTO;
import com.expense.tracker.exception.GlobalExceptionHandler;
import com.expense.tracker.exception.ResourceNotFoundException;
import com.expense.tracker.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
@Import(GlobalExceptionHandler.class)
class TransactionControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean TransactionService transactionService;

    private static final Long USER_ID = 10L;
    private static final Long TRX_ID = 1L;
    private static final String BASE_URL = "/api/transactions";

    private static final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor USER_JWT =
            jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"));

    private TransactionDTO transactionDTO;

    @BeforeEach
    void setup() {
        transactionDTO = TransactionDTO.builder()
                .id(TRX_ID)
                .userId(USER_ID)
                .note("Salary")
                .amount(new BigDecimal("5000.00"))
                .type("INCOME")
                .category("Salary")
                .date(LocalDateTime.now())
                .remarks("")
                .build();
    }

    @Test
    void create_success_returns201() throws Exception {
        when(transactionService.create(any(), any(TransactionDTO.class)))
                .thenReturn(transactionDTO);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDTO))
                        .with(USER_JWT))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(TRX_ID));
    }

    @Test
    void create_invalidBody_returns400() throws Exception {
        String body = """
            {
               "note": "Salary",
               "amount": -5000.00,
               "type": "INCOME",
               "category": "Salary",
               "date": "2026-09-18T00:00:00",
               "remarks": ""
            }
            """;

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .with(USER_JWT))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.SYS_BAD_REQUEST.getCode()))
                .andExpect(jsonPath("$.violations[0].field").value("amount"));
    }

    @Test
    void getById_found_returns200() throws Exception {
        when(transactionService.getById(any(), eq(TRX_ID))).thenReturn(transactionDTO);

        mockMvc.perform(get(BASE_URL + "/" + TRX_ID).with(USER_JWT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TRX_ID))
                .andExpect(jsonPath("$.amount").value(5000.00));
    }

    @Test
    void getById_notFound_returns404() throws Exception {
        when(transactionService.getById(any(), eq(TRX_ID)))
                .thenThrow(new ResourceNotFoundException(ErrorCode.TRX_NOT_FOUND));

        mockMvc.perform(get(BASE_URL + "/" + TRX_ID).with(USER_JWT))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ErrorCode.TRX_NOT_FOUND.getCode()));
    }

    @Test
    void getById_noToken_returns401() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + TRX_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void update_success_returns200() throws Exception {
        when(transactionService.update(any(), eq(TRX_ID), any())).thenReturn(transactionDTO);

        mockMvc.perform(put(BASE_URL + "/" + TRX_ID)
                        .with(USER_JWT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TRX_ID));
    }

    @Test
    void update_invalidBody_returns400() throws Exception {
        String body = """
            {
               "note": "Salary",
               "amount": -5000.00,
               "type": "INCOME",
               "category": "Salary",
               "date": "2026-09-18T00:00:00",
               "remarks": ""
            }
            """;

        mockMvc.perform(put(BASE_URL + "/" + TRX_ID)
                        .with(USER_JWT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.SYS_BAD_REQUEST.getCode()))
                .andExpect(jsonPath("$.violations[0].field").value("amount"));
    }

    @Test
    void delete_success_returns204() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/" + TRX_ID).with(USER_JWT))
                .andExpect(status().isNoContent());
    }


}