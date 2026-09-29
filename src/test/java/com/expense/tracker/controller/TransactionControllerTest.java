package com.expense.tracker.controller;

import com.expense.tracker.constant.ErrorCode;
import com.expense.tracker.dto.TransactionDTO;
import com.expense.tracker.entity.Transaction;
import com.expense.tracker.exception.GlobalExceptionHandler;
import com.expense.tracker.service.CurrentUserService;
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
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
@Import(GlobalExceptionHandler.class)
class TransactionControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;  // ✅ 用 Spring 的真实实例

    @MockBean TransactionService transactionService;
    @MockBean CurrentUserService currentUserService;

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
}