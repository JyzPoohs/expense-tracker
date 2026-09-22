package com.expense.tracker.entity;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record BudgetUpdateRequest(
        @NotNull @DecimalMin("0.00") BigDecimal amount
) { }
