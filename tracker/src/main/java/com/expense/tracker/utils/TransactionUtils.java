package com.expense.tracker.utils;

import com.expense.tracker.constant.TransactionType;
import com.expense.tracker.dto.TransactionDTO;

import java.math.BigDecimal;
import java.util.List;

public final class TransactionUtils {
    public static BigDecimal calculateTotal(List<TransactionDTO> transactions, String type) {
        return transactions.stream()
                .filter((transaction)-> type.equalsIgnoreCase(transaction.getType()))
                .map(TransactionDTO::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
