package com.expense.tracker.service;

import com.expense.tracker.constant.ErrorCode;
import com.expense.tracker.dto.TransactionDTO;
import com.expense.tracker.entity.Transaction;
import com.expense.tracker.exception.ResourceNotFoundException;
import com.expense.tracker.mapper.TransactionMapper;
import com.expense.tracker.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock TransactionRepository transactionRepository;
    @Mock TransactionMapper transactionMapper;
    @Mock CurrentUserService currentUserService;
    @Mock Jwt jwt;

    @InjectMocks TransactionService transactionService;

    private static final Long USER_ID = 10L;
    private static final Long TRX_ID  = 1L;

    private Transaction transaction;
    private TransactionDTO transactionDTO;

    @BeforeEach
    void setup() {
        transaction = Transaction.builder()
                .id(TRX_ID)
                .userId(USER_ID)
                .note("Salary")
                .amount(new BigDecimal("5000.00"))
                .type("INCOME")
                .category("Salary")
                .date(LocalDateTime.now())
                .remarks("")
                .build();

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
    void getById_existsAndOwned_returnsDTO() {
        when(currentUserService.getCurrentUserId(jwt)).thenReturn(USER_ID);
        when(transactionRepository.findByIdAndUserId(TRX_ID, USER_ID)).thenReturn(Optional.of(transaction));
        when(transactionMapper.toDTO(transaction)).thenReturn(transactionDTO);

        TransactionDTO result = transactionService.getById(jwt, TRX_ID);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(TRX_ID);
        assertThat(result.getUserId()).isEqualTo(USER_ID);
    }

    @Test
    void getById_notFound_throwsResourceNotFoundException() {
        when(currentUserService.getCurrentUserId(jwt)).thenReturn(USER_ID);
        when(transactionRepository.findByIdAndUserId(TRX_ID, USER_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.getById(jwt, TRX_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(ErrorCode.TRX_NOT_FOUND.getDefaultMessage());
    }


}
