package com.expense.tracker.repository;

import com.expense.tracker.entity.SystemCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemCategoryRepository extends JpaRepository<SystemCategory, Long> {
}
