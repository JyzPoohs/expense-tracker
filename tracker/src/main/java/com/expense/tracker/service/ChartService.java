package com.expense.tracker.service;

import com.expense.tracker.dto.DashboardBarChartDTO;
import com.expense.tracker.dto.TransactionDTO;
import com.expense.tracker.mapper.TransactionMapper;
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
    private final TransactionMapper mapper;

    public ChartService(TransactionService transactionService, TransactionMapper mapper) {
        this.transactionService = transactionService;
        this.mapper = mapper;
    }

    public List<DashboardBarChartDTO> getDashboardBarChartData(Jwt jwt) {
        LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), LocalDate.now().lengthOfMonth());
        LocalDate endDate = LocalDate.of(LocalDateTime.now().getYear(), LocalDateTime.now().minusMonths(5).getMonthValue(),1);
        List<TransactionDTO> transactions = transactionService.getByDateBetween(jwt, startDate, endDate);

        List<DashboardBarChartDTO> dashboardBarChartData = new ArrayList<>();

        for(int i = 5; i > 0; i--) {
            String month = LocalDate.now().minusMonths(i).getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

            DashboardBarChartDTO barChartData = DashboardBarChartDTO.builder()
                    .month(month)
                    .expense(BigDecimal.valueOf(1))
                    .income(BigDecimal.valueOf(1))
                    .build();

            dashboardBarChartData.add(barChartData);
        }

        return dashboardBarChartData;
    }
}
