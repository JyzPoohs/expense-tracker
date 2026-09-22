package com.expense.tracker.repository;

import com.expense.tracker.entity.Budget;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<Budget> findByIdAndUserId(Long id, Long userId);

    Optional<Budget> findByUserIdAndCategoryIdIsNullAndMonthAndYear(Long userId, @NotNull @Min(1) @Max(12) int month, @Min(2000) int year);

    List<Budget> findByUserIdAndCategoryIdIsNotNullAndMonthAndYear(Long userId, @NotNull @Min(1) @Max(12) int month, @Min(2000) int year);

    boolean existsByUserIdAndCategoryIdIsNullAndMonthAndYear(Long userId, @NotNull @Min(1) @Max(12) int month, @Min(2000) int year);

    boolean existsByUserIdAndCategoryIdAndMonthAndYear(Long userId, Long categoryId, @NotNull @Min(1) @Max(12) int month, @Min(2000) int year);
}
