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

        when(currentUserService.getCurrentUserId(jwt)).thenReturn(USER_ID);
    }

    @Test
    void getById_existsAndOwned_returnsDTO() {
        when(transactionRepository.findByIdAndUserId(TRX_ID, USER_ID)).thenReturn(Optional.of(transaction));
        when(transactionMapper.toDTO(transaction)).thenReturn(transactionDTO);

        TransactionDTO result = transactionService.getById(jwt, TRX_ID);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(TRX_ID);
        assertThat(result.getUserId()).isEqualTo(USER_ID);
    }

    @Test
    void getById_notFound_throwsResourceNotFoundException() {
        when(transactionRepository.findByIdAndUserId(TRX_ID, USER_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.getById(jwt, TRX_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(ErrorCode.TRX_NOT_FOUND.getDefaultMessage());
    }

    @Test
    void create_validInput_savesAndReturnsDTO() {
        when(transactionMapper.toEntity(any(TransactionDTO.class))).thenReturn(transaction);
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        when(transactionMapper.toDTO(transaction)).thenReturn(transactionDTO);

        TransactionDTO result = transactionService.create(jwt, transactionDTO);

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(USER_ID);

        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void update_ownedTransaction_updatesAndReturnsDTO() {
        TransactionDTO updateRequest = TransactionDTO.builder()
                .note("Updated note")
                .amount(new BigDecimal("6000.00"))
                .type("INCOME")
                .category("Bonus")
                .date(LocalDateTime.now())
                .remarks("year-end bonus")
                .build();

        when(transactionRepository.findByIdAndUserId(TRX_ID, USER_ID)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        when(transactionMapper.toDTO(transaction)).thenReturn(transactionDTO);

        TransactionDTO result = transactionService.update(jwt, TRX_ID, updateRequest);

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(USER_ID);

        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void update_notFound_throwsResourceNotFoundException() {
        when(transactionRepository.findByIdAndUserId(TRX_ID, USER_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.update(jwt, TRX_ID, transactionDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(ErrorCode.TRX_NOT_FOUND.getDefaultMessage());

        verify(transactionRepository, never()).save(any());
    }

    @Test
    void delete_ownedTransaction_deletesSuccessfully() {
        when(transactionRepository.findByIdAndUserId(TRX_ID, USER_ID)).thenReturn(Optional.of(transaction));

        transactionService.delete(jwt, TRX_ID);

        verify(transactionRepository).delete(transaction);
    }

    @Test
    void delete_notFound_throwsAndNeverDeletes() {
        when(transactionRepository.findByIdAndUserId(TRX_ID, USER_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.delete(jwt, TRX_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(ErrorCode.TRX_NOT_FOUND.getDefaultMessage());

        verify(transactionRepository, never()).delete(any());

    }





}
