package com.expense.tracker.repository;

import com.expense.tracker.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<Budget> findByIdAndUserId(Long id, Long userId);

    Optional<Budget> findByUserIdAndCategoryIdIsNullAndMonthAndYear(Long userId, int month, int year);

    List<Budget> findByUserIdAndCategoryIdIsNotNullAndMonthAndYear(Long userId, int month,int year);

    boolean existsByUserIdAndCategoryIdIsNullAndMonthAndYear(Long userId, int month, int year);

    boolean existsByUserIdAndCategoryIdAndMonthAndYear(Long userId, Long categoryId, int month, int year);
}
