package com.expense.tracker.repository;

import com.expense.tracker.entity.SystemCategoryPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SystemCategoryPreferenceRepository extends JpaRepository<SystemCategoryPreference, Long> {
    Optional<SystemCategoryPreference> findByUserId(Long userId);
}
