package com.expense.tracker.service;

import com.expense.tracker.constant.TransactionType;
import com.expense.tracker.dto.DashboardSummaryDTO;
import com.expense.tracker.dto.TransactionDTO;
import jakarta.transaction.Transactional;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@Service
@Transactional
public class SummaryService {
    private final TransactionService transactionService;
    private final CurrentUserService currentUserService;

    public SummaryService(TransactionService transactionService, CurrentUserService currentUserService) {
        this.transactionService = transactionService;
        this.currentUserService = currentUserService;
    }

    public DashboardSummaryDTO getDashboardSummary(Jwt jwt, String type, String category, Integer month, Integer year) {
        List<TransactionDTO> transactionDTOS = transactionService.getAll(jwt, type, category, month, year);

        // Calculate dashboard summary for current month
        Month currentMonth = LocalDateTime.now().getMonth();

        List<TransactionDTO> currentTransactions = transactionDTOS.stream()
                .filter((transactionDTO -> transactionDTO.getDate().getMonth().equals(currentMonth))).toList();


        Long numTransactions = (long) currentTransactions.size();

        BigDecimal currentTotalIncome = (BigDecimal) currentTransactions.stream()
                .filter((transaction) -> transaction.getType().equalsIgnoreCase(TransactionType.INCOME))
                .map(TransactionDTO::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal currentTotalExpense = (BigDecimal) currentTransactions.stream()
                .filter((transaction) -> transaction.getType().equalsIgnoreCase(TransactionType.EXPENSE))
                .map(TransactionDTO::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal currentTotalBalance = currentTotalIncome.subtract(currentTotalExpense);

        // Calculate dashboard summary for previous month
        Month previousMonth = Month.of(LocalDateTime.now().getMonthValue() - 1);

        List<TransactionDTO> previousTransactions = transactionDTOS.stream()
                .filter((transactionDTO -> transactionDTO.getDate().getMonth().equals(previousMonth))).toList();


        BigDecimal previousTotalIncome = (BigDecimal) previousTransactions.stream()
                .filter((transaction) -> transaction.getType().equalsIgnoreCase(TransactionType.INCOME))
                .map(TransactionDTO::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal previousTotalExpense = (BigDecimal) previousTransactions.stream()
                .filter((transaction) -> transaction.getType().equalsIgnoreCase(TransactionType.EXPENSE))
                .map(TransactionDTO::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

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
