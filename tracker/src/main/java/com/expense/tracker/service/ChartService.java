package com.expense.tracker.service;

import com.expense.tracker.constant.TransactionType;
import com.expense.tracker.dto.DashboardBarChartDTO;
import com.expense.tracker.dto.TransactionDTO;
import jakarta.transaction.Transactional;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class ChartService {
    private final TransactionService transactionService;

    public ChartService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public List<DashboardBarChartDTO> getDashboardBarChartData(Jwt jwt) {
        LocalDateTime startDate = LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), LocalDate.now().lengthOfMonth(), 0, 0);
        LocalDateTime endDate = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().minusMonths(5).getMonthValue(),1, 0, 0);
        List<TransactionDTO> transactions = transactionService.getByDateBetween(jwt, startDate, endDate);

        List<DashboardBarChartDTO> dashboardBarChartData = new ArrayList<>();

        for(int i = 5; i >= 0; i--) {
            String month = LocalDate.now().minusMonths(i).getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

            List<TransactionDTO> groupedTransactionsByMonth = transactions.stream()
                    .filter((transaction)-> month.equalsIgnoreCase(String.valueOf(transaction.getDate().getMonth())))
                    .toList();

            BigDecimal totalExpense = groupedTransactionsByMonth.stream()
                    .filter((transaction)-> TransactionType.EXPENSE.equalsIgnoreCase(transaction.getType()))
                    .map(TransactionDTO::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalIncome = groupedTransactionsByMonth.stream()
                    .filter((transaction)-> TransactionType.INCOME.equalsIgnoreCase(transaction.getType()))
                    .map(TransactionDTO::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            DashboardBarChartDTO barChartData = DashboardBarChartDTO.builder()
                    .month(month)
                    .expense(totalExpense)
                    .income(totalIncome)
                    .build();

            dashboardBarChartData.add(barChartData);
        }

        return dashboardBarChartData;
    }
}
