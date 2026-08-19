package com.expense.tracker.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardPieChartDTO {
    private String expense;
    private BigDecimal total;
}
