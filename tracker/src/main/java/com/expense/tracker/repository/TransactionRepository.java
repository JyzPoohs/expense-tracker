package com.expense.tracker.repository;

import com.expense.tracker.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserId(Long userId);

    @Query("""
            SELECT t
            FROM Transaction t
            WHERE t.userId = :userId
            AND (:type IS NULL OR t.type = :type)
            AND (:category IS NULL OR t.category = :category)
            AND (:startDate IS NULL OR t.date >= :startDate)
            AND (:endDate IS NULL OR t.date <= :endDate)
            ORDER BY t.date DESC
            """)
    List<Transaction> findByFilterOptions(
            Long userId,
            String type,
            String category,
            LocalDateTime startDate,
            LocalDateTime endDate);
}
