package com.expense.tracker.service;

import com.expense.tracker.constant.ErrorCode;
import com.expense.tracker.dto.TransactionDTO;
import com.expense.tracker.entity.Transaction;
import com.expense.tracker.exception.ResourceNotFoundException;
import com.expense.tracker.mapper.TransactionMapper;
import com.expense.tracker.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper mapper;

    public TransactionService(TransactionRepository transactionRepository, TransactionMapper mapper) {
        this.transactionRepository = transactionRepository;
        this.mapper = mapper;
    }

    public TransactionDTO create(TransactionDTO transactionDTO) {
        Transaction transaction = mapper.toEntity(transactionDTO);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());

        return mapper.toDTO(transactionRepository.save(transaction));
    }

    public TransactionDTO getById(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(ErrorCode.TRANSACTION_NOT_FOUND, "Transaction not found with id: " + id));

        return mapper.toDTO(transaction);
    }

    public List<TransactionDTO> getAll() {
        return transactionRepository
                .findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    public TransactionDTO update(Long id, TransactionDTO transactionDTO) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(ErrorCode.TRANSACTION_NOT_FOUND,  "Transaction not found with id: " + 1));

        transaction.setDescription(transactionDTO.getDescription());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setType(transactionDTO.getType());
        transaction.setCategory(transactionDTO.getCategory());
        transaction.setUpdatedAt(LocalDateTime.now());

        return mapper.toDTO(transactionRepository.save(transaction));
    }

    public void delete(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(ErrorCode.TRANSACTION_NOT_FOUND, "Transaction not found with id: " + id));

        transactionRepository.delete(transaction);
    }
}
