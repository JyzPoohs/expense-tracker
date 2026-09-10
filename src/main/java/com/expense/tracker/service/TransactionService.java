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
    private final CurrentUserService currentUserService;

    public TransactionService(TransactionRepository transactionRepository, TransactionMapper mapper, CurrentUserService currentUserService) {
        this.transactionRepository = transactionRepository;
        this.mapper = mapper;
        this.currentUserService = currentUserService;
    }

    public TransactionDTO create(Jwt jwt, TransactionDTO transactionDTO) {
        transactionDTO.setUser_id(currentUserService.getCurrentUser(jwt).getId());
        Transaction transaction = mapper.toEntity(transactionDTO);
        return mapper.toDTO(transactionRepository.save(transaction));
    }

    public TransactionDTO getById(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(ErrorCode.TRANSACTION_NOT_FOUND, "Transaction not found with id: " + id));

        return mapper.toDTO(transaction);
    }

    public List<TransactionDTO> getAll(Jwt jwt, String type, String category, Integer month, Integer year) {
        Long userId = currentUserService.getCurrentUserId(jwt);

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
                new ResourceNotFoundException(ErrorCode.TRANSACTION_NOT_FOUND, "Transaction not found with id: " + id));

        transaction.setNote(transactionDTO.getNote());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setType(transactionDTO.getType());
        transaction.setCategory(transactionDTO.getCategory());
        transaction.setRemarks(transactionDTO.getRemarks());
        transaction.setDate(transactionDTO.getDate());

        return mapper.toDTO(transactionRepository.save(transaction));
    }

    public void delete(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(ErrorCode.TRANSACTION_NOT_FOUND, "Transaction not found with id: " + id));

        transactionRepository.delete(transaction);
    }

    public List<TransactionDTO> getByDateBetween(Jwt jwt, LocalDateTime startDate, LocalDateTime endDate) {
        Long userId = currentUserService.getCurrentUserId(jwt);

        return transactionRepository.findByUserIdAndDateBetween(userId, startDate, endDate).stream().map(mapper::toDTO).toList();
    }
}
