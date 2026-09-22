package com.expense.tracker.mapper;

import com.expense.tracker.dto.BudgetDTO;
import com.expense.tracker.entity.Budget;
import org.springframework.stereotype.Component;

@Component
public class BudgetMapper {
    public BudgetDTO toDTO(Budget budget) {
        return  BudgetDTO.builder()
                .id(budget.getId())
                .userId(budget.getUserId())
                .categoryId(budget.getCategoryId())
                .amount(budget.getAmount())
                .month(budget.getMonth())
                .year(budget.getYear())
                .build();
    }
}
