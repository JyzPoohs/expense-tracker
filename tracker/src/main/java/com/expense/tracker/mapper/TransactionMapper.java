package com.expense.tracker.mapper;

import com.expense.tracker.dto.TransactionDTO;
import com.expense.tracker.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    public TransactionDTO toDTO(Transaction transaction) {
        return TransactionDTO.builder()
                .note(transaction.getNote())
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .category(transaction.getCategory())
                .date(transaction.getDate())
                .remarks(transaction.getRemarks())
                .build();
    }

    public Transaction toEntity(TransactionDTO transactionDTO) {
        return Transaction.builder()
                .note(transactionDTO.getNote())
                .amount(transactionDTO.getAmount())
                .type(transactionDTO.getType())
                .category(transactionDTO.getCategory())
                .date(transactionDTO.getDate())
                .remarks(transactionDTO.getRemarks())
                .build();
    }
}
