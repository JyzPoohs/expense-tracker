package com.expense.tracker.service;

import com.expense.tracker.constant.ErrorCode;
import com.expense.tracker.dto.BudgetDTO;
import com.expense.tracker.entity.Budget;
import com.expense.tracker.dto.BudgetUpdateRequest;
import com.expense.tracker.exception.ConflictException;
import com.expense.tracker.exception.ResourceNotFoundException;
import com.expense.tracker.mapper.BudgetMapper;
import com.expense.tracker.repository.BudgetRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static com.expense.tracker.config.CacheConfig.CACHE_BUDGETS_CATEGORY;
import static com.expense.tracker.config.CacheConfig.CACHE_BUDGETS_OVERALL;

@Service
public class BudgetService {
    private final CurrentUserService currentUserService;
    private final BudgetMapper budgetMapper;
    private final BudgetRepository budgetRepository;

    public BudgetService(CurrentUserService currentUserService, BudgetMapper budgetMapper, BudgetRepository budgetRepository) {
        this.currentUserService = currentUserService;
        this.budgetMapper = budgetMapper;
        this.budgetRepository = budgetRepository;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = CACHE_BUDGETS_OVERALL, key = "#jwt.subject + ':' + #month + ':' + #year")
    public BudgetDTO getOverallBudget(Jwt jwt, int month, int year) {

        Long userId = currentUserService.getCurrentUserId(jwt);

        Budget budget = budgetRepository
                .findByUserIdAndCategoryIdIsNullAndMonthAndYear(userId, month, year)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.BDG_NOT_FOUND,
                        String.format("Overall budget not found for %02d/%d", month, year)
                ));

        return budgetMapper.toDTO(budget);
    }


    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {
            @CacheEvict(value = CACHE_BUDGETS_OVERALL,  allEntries = true),
            @CacheEvict(value = CACHE_BUDGETS_CATEGORY, allEntries = true)
    })
    public BudgetDTO createOverallBudget(Jwt jwt, int month, int year) {

        Long userId = currentUserService.getCurrentUserId(jwt);

        boolean exists = budgetRepository.existsByUserIdAndCategoryIdIsNullAndMonthAndYear(userId, month, year);

        if (exists) {
            throw new ConflictException(
                    ErrorCode.BDG_ALREADY_EXISTS,
                    String.format("Overall budget already exists for %02d/%d", month, year)
            );
        }

        Budget budget = Budget.builder()
                .userId(userId)
                .categoryId(null)
                .amount(BigDecimal.ZERO)
                .month(month)
                .year(year)
                .build();

        Budget savedBudget = budgetRepository.save(budget);

        return budgetMapper.toDTO(savedBudget);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = CACHE_BUDGETS_CATEGORY, key = "#jwt.subject + ':' + #month + ':' + #year")
    public List<BudgetDTO> getCategoryBudgets(Jwt jwt, int month, int year) {
        return budgetRepository.findByUserIdAndCategoryIdIsNotNullAndMonthAndYear(currentUserService.getCurrentUserId(jwt), month, year)
                .stream().map(budgetMapper::toDTO).toList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {
            @CacheEvict(value = CACHE_BUDGETS_OVERALL,  allEntries = true),
            @CacheEvict(value = CACHE_BUDGETS_CATEGORY, allEntries = true)
    })
    public BudgetDTO createCategoryBudget(Jwt jwt, Long categoryId, int month, int year) {
        Long userId = currentUserService.getCurrentUserId(jwt);

        boolean exists = budgetRepository.existsByUserIdAndCategoryIdAndMonthAndYear(userId, categoryId, month, year);

        if (exists) {
            throw new ConflictException(
                    ErrorCode.BDG_ALREADY_EXISTS,
                    String.format("This category budget already exists for %02d/%d", month, year)
            );
        }

        Budget budget = Budget.builder()
                .userId(userId)
                .categoryId(categoryId)
                .amount(BigDecimal.ZERO)
                .month(month)
                .year(year)
                .build();

        Budget savedBudget = budgetRepository.save(budget);

        return budgetMapper.toDTO(savedBudget);
    }

    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {
            @CacheEvict(value = CACHE_BUDGETS_OVERALL,  allEntries = true),
            @CacheEvict(value = CACHE_BUDGETS_CATEGORY, allEntries = true)
    })
    public BudgetDTO update(Jwt jwt, Long id, BudgetUpdateRequest budgetDTO) {
        Long userId = currentUserService.getCurrentUserId(jwt);

        Budget budget = budgetRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BDG_NOT_FOUND));

        budget.setAmount(budgetDTO.amount());

        return budgetMapper.toDTO(budgetRepository.save(budget));
    }

    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {
            @CacheEvict(value = CACHE_BUDGETS_OVERALL,  allEntries = true),
            @CacheEvict(value = CACHE_BUDGETS_CATEGORY, allEntries = true)
    })
    public void delete(Jwt jwt, Long id) {
        Long userId = currentUserService.getCurrentUserId(jwt);

        Budget budget = budgetRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BDG_NOT_FOUND));

        budgetRepository.delete(budget);
    }
}
