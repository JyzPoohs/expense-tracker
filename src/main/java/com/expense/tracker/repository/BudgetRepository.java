package com.expense.tracker.repository;

import com.expense.tracker.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<Budget> findByUserIdAndCategoryIdIsNullAndMonthAndYear(Long userId, int month, int year);
}
