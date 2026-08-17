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
import java.time.LocalTime;
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
        LocalDate today = LocalDate.now();

        LocalDateTime startDate = today
                .minusMonths(5)
                .withDayOfMonth(1)
                .atStartOfDay();

        LocalDateTime endDate = today
                .withDayOfMonth(today.lengthOfMonth())
                .atTime(LocalTime.MAX);

        List<TransactionDTO> transactions = transactionService.getByDateBetween(jwt, startDate, endDate);

        for(TransactionDTO t: transactions) {
            System.out.println("log: " + t.toString());
        }

        List<DashboardBarChartDTO> dashboardBarChartData = new ArrayList<>();

        for(int i = 5; i >= 0; i--) {
            String month = LocalDate.now().minusMonths(i).getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

            List<TransactionDTO> groupedTransactionsByMonth = transactions.stream()
                    .filter((transaction)-> month.equalsIgnoreCase(String.valueOf(transaction.getDate().getMonth())))
                    .toList();

            for(TransactionDTO t: groupedTransactionsByMonth) {
                System.out.println(t);
            }

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
