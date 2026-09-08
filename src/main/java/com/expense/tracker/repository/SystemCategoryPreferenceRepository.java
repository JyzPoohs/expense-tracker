package com.expense.tracker.repository;

import com.expense.tracker.entity.SystemCategoryPreference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemCategoryPreferenceRepository extends JpaRepository<SystemCategoryPreference, Long> {
    SystemCategoryPreference findByUserId(Long userId);
}
