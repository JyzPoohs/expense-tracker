package com.expense.tracker.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetDTO {
    private Long categoryId;
    @NotNull @Positive(message = "Budget must be greater than zero")
    private BigDecimal budget;
    @NotNull @Min(1) @Max(12)
    private Integer month;
    @Min(2000)
    private Integer year;

}
