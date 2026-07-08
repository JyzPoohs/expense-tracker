package com.expense.tracker.service;

import com.expense.tracker.constant.ErrorCode;
import com.expense.tracker.dto.TransactionDTO;
import com.expense.tracker.entity.Transaction;
import com.expense.tracker.exception.ResourceNotFoundException;
import com.expense.tracker.mapper.TransactionMapper;
import com.expense.tracker.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper mapper;
    private final AuthService authService;

    public TransactionService(TransactionRepository transactionRepository, TransactionMapper mapper, AuthService authService) {
        this.transactionRepository = transactionRepository;
        this.mapper = mapper;
        this.authService = authService;
    }

    public TransactionDTO create(Jwt jwt, TransactionDTO transactionDTO) {
        transactionDTO.setUser_id(authService.getCurrentUser(jwt).getId());
        Transaction transaction = mapper.toEntity(transactionDTO);
        return mapper.toDTO(transactionRepository.save(transaction));
    }

    public TransactionDTO getById(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(ErrorCode.TRANSACTION_NOT_FOUND, "Transaction not found with id: " + id));

        return mapper.toDTO(transaction);
    }

    public List<TransactionDTO> getAll(Jwt jwt, String type, String category, Integer month, Integer year) {
        Long userId = authService.getCurrentUserId(jwt);

        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        if (month != null && year != null) {
            LocalDate firstDay = LocalDate.of(year, month, 1);
            LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());

            startDate = firstDay.atStartOfDay();
            endDate = lastDay.atTime(LocalTime.MAX);
        }

        if (type != null && type.isBlank()) {
            type = null;
        }

        if (category != null && category.isBlank()) {
            category = null;
        }

        return transactionRepository.findByFilterOptions(userId, type, category, startDate, endDate)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    public TransactionDTO update(Long id, TransactionDTO transactionDTO) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(ErrorCode.TRANSACTION_NOT_FOUND, "Transaction not found with id: " + 1));

        transaction.setNote(transactionDTO.getNote());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setType(transactionDTO.getType());
        transaction.setCategory(transactionDTO.getCategory());
        transaction.setRemarks(transactionDTO.getRemarks());

        return mapper.toDTO(transactionRepository.save(transaction));
    }

    public void delete(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(ErrorCode.TRANSACTION_NOT_FOUND, "Transaction not found with id: " + id));

        transactionRepository.delete(transaction);
    }

    public List<TransactionDTO> getByType(String type) {
        return transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getType().equals(type)).map(mapper::toDTO).toList();
    }

    public List<TransactionDTO> getByCategory(String category) {
        return transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getCategory().equals(category)).map(mapper::toDTO).toList();
    }
}
