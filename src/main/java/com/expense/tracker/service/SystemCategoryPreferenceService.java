package com.expense.tracker.service;

import com.expense.tracker.entity.SystemCategoryPreference;
import com.expense.tracker.repository.SystemCategoryPreferenceRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class SystemCategoryPreferenceService {
    private final SystemCategoryPreferenceRepository systemCategoryPreferenceRepository;

    public SystemCategoryPreferenceService(SystemCategoryPreferenceRepository systemCategoryPreferenceRepository) {
        this.systemCategoryPreferenceRepository = systemCategoryPreferenceRepository;
    }

    private String createPreference() {
        return "{  \"hiddenCategories\": [],  \"customColors\": {}  }";
    }

    public SystemCategoryPreference createSystemCategoryPreference(Long userId) {
        SystemCategoryPreference preference = SystemCategoryPreference.builder()
                .userId(userId)
                .preferences(createPreference())
                .build();

        return systemCategoryPreferenceRepository.save(preference);
    }
}
