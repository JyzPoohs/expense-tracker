package com.expense.tracker.service;

import com.expense.tracker.constant.ErrorCode;
import com.expense.tracker.dto.BudgetDTO;
import com.expense.tracker.entity.Budget;
import com.expense.tracker.exception.ResourceNotFoundException;
import com.expense.tracker.mapper.BudgetMapper;
import com.expense.tracker.repository.BudgetRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class BudgetService {
    private final CurrentUserService currentUserService;
    private final BudgetMapper budgetMapper;
    private final BudgetRepository budgetRepository;

    public BudgetService(CurrentUserService currentUserService, BudgetMapper budgetMapper, BudgetRepository budgetRepository) {
        this.currentUserService = currentUserService;
        this.budgetMapper = budgetMapper;
        this.budgetRepository = budgetRepository;
    }

    public BudgetDTO getOverall(Jwt jwt, int month, int year) {
        Long userId = currentUserService.getCurrentUserId(jwt);

        Budget budget = budgetRepository
                .findByUserIdAndCategoryIdIsNullAndMonthAndYear(userId, month, year)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.BDG_NOT_FOUND,
                        String.format("Overall budget not found for %02d/%d", month, year)
                ));

        return budgetMapper.toDTO(budget);
    }

    public BudgetDTO createOverall(Jwt jwt, int month, int year) {
        Budget budget = Budget.builder()
                .userId(currentUserService.getCurrentUserId(jwt))
                .categoryId(null)
                .budget(BigDecimal.ZERO)
                .month(month)
                .year(year)
                .build();

        return budgetMapper.toDTO(budgetRepository.save(budget));
    }
}
