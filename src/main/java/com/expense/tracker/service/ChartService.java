package com.expense.tracker.service;

import com.expense.tracker.constant.TransactionType;
import com.expense.tracker.dto.DashboardBarChartDTO;
import com.expense.tracker.dto.DashboardPieChartDTO;
import com.expense.tracker.dto.TransactionDTO;
import com.expense.tracker.utils.TransactionUtils;
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
import java.util.Map;
import java.util.stream.Collectors;

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

        List<DashboardBarChartDTO> dashboardBarChartData = new ArrayList<>();

        for(int i = 5; i >= 0; i--) {
            String month = LocalDate.now().minusMonths(i).getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

            List<TransactionDTO> groupedTransactionsByMonth = transactions.stream()
                    .filter((transaction)-> month.equalsIgnoreCase(String.valueOf(transaction.getDate().getMonth())))
                    .toList();

            for(TransactionDTO t: groupedTransactionsByMonth) {
                System.out.println(t);
            }

            BigDecimal totalExpense = TransactionUtils.calculateTotal(groupedTransactionsByMonth, TransactionType.EXPENSE);

            BigDecimal totalIncome = TransactionUtils.calculateTotal(groupedTransactionsByMonth, TransactionType.INCOME);

            DashboardBarChartDTO barChartData = DashboardBarChartDTO.builder()
                    .month(month)
                    .expense(totalExpense)
                    .income(totalIncome)
                    .build();

            dashboardBarChartData.add(barChartData);
        }

        return dashboardBarChartData;
    }

    public List<DashboardPieChartDTO> getDashboardPieChartData(Jwt jwt) {
        LocalDate today = LocalDate.now();
        List<TransactionDTO> transactions = transactionService.getAll(jwt, null, null, today.getMonthValue(), today.getYear());

        List<DashboardPieChartDTO> dashboardPieChartData = new ArrayList<>();

        Map<String, BigDecimal> groupedTransactionsByCategory = transactions.stream()
                .filter(transaction -> TransactionType.EXPENSE.equalsIgnoreCase(transaction.getType()))
                .collect(Collectors.groupingBy(TransactionDTO::getCategory,
                        Collectors.reducing(BigDecimal.ZERO, TransactionDTO::getAmount, BigDecimal::add)));

        for(Map.Entry<String, BigDecimal> groupedTransaction: groupedTransactionsByCategory.entrySet()) {
            DashboardPieChartDTO dashboardPieChartDTO = DashboardPieChartDTO.builder()
                    .expense(groupedTransaction.getKey())
                    .total(groupedTransaction.getValue()).build();

            dashboardPieChartData.add(dashboardPieChartDTO);
        }

        return dashboardPieChartData;
    }
}
