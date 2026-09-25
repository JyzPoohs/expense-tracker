package com.expense.tracker.service;

import com.expense.tracker.constant.TransactionType;
import com.expense.tracker.dto.DashboardSummaryDTO;
import com.expense.tracker.dto.TransactionDTO;
import com.expense.tracker.utils.TransactionUtils;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class SummaryService {
    private final TransactionService transactionService;

    public SummaryService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public DashboardSummaryDTO getDashboardSummary(Jwt jwt, String type, String category, Integer month, Integer year) {
        List<TransactionDTO> transactionDTOS = transactionService.getAll(jwt, type, category, month, year);

        // Calculate dashboard summary for current month
        Month currentMonth = LocalDateTime.now().getMonth();

        List<TransactionDTO> currentTransactions = transactionDTOS.stream()
                .filter((transactionDTO -> transactionDTO.getDate().getMonth().equals(currentMonth))).toList();


        Long numTransactions = (long) currentTransactions.size();

        BigDecimal currentTotalIncome = TransactionUtils.calculateTotal(currentTransactions, TransactionType.INCOME);

        BigDecimal currentTotalExpense = TransactionUtils.calculateTotal(currentTransactions, TransactionType.EXPENSE);

        BigDecimal currentTotalBalance = currentTotalIncome.subtract(currentTotalExpense);

        // Calculate dashboard summary for previous month
        Month previousMonth = LocalDateTime.now().getMonth().minus(1);;

        List<TransactionDTO> previousTransactions = transactionDTOS.stream()
                .filter((transactionDTO -> transactionDTO.getDate().getMonth().equals(previousMonth))).toList();


        BigDecimal previousTotalIncome = TransactionUtils.calculateTotal(previousTransactions, TransactionType.INCOME);

        BigDecimal previousTotalExpense = TransactionUtils.calculateTotal(previousTransactions, TransactionType.EXPENSE);

        BigDecimal previousTotalBalance = previousTotalIncome.subtract(previousTotalExpense);

        BigDecimal percentage;
        if(previousTotalBalance.compareTo(BigDecimal.ZERO) == 0) {
            percentage = BigDecimal.ZERO;
        } else {
            percentage = currentTotalBalance.subtract(previousTotalBalance)
                    .divide(previousTotalBalance, 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }

        return DashboardSummaryDTO.builder()
                .totalBalance(currentTotalBalance)
                .totalIncome(currentTotalIncome)
                .totalExpense(currentTotalExpense)
                .numTransactions(numTransactions)
                .percentage(percentage)
                .build();
    }
}
