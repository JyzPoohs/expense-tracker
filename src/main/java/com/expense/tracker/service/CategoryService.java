package com.expense.tracker.service;

import com.expense.tracker.constant.ErrorCode;
import com.expense.tracker.dto.CategoryDTO;
import com.expense.tracker.dto.SystemCategoryPreferenceDetail;
import com.expense.tracker.entity.Category;
import com.expense.tracker.entity.SystemCategory;
import com.expense.tracker.entity.SystemCategoryPreference;
import com.expense.tracker.exception.SystemException;
import com.expense.tracker.mapper.CategoryMapper;
import com.expense.tracker.repository.CategoryRepository;
import com.expense.tracker.repository.SystemCategoryPreferenceRepository;
import com.expense.tracker.repository.SystemCategoryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(rollbackFor = Exception.class)
public class CategoryService {
    private final ObjectMapper objectMapper;
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;
    private final SystemCategoryRepository systemCategoryRepository;
    private final SystemCategoryPreferenceRepository systemCategoryPreferenceRepository;
    private final CurrentUserService currentUserService;

    public CategoryService(ObjectMapper objectMapper, CategoryMapper categoryMapper, CategoryRepository categoryRepository, SystemCategoryRepository systemCategoryRepository, SystemCategoryPreferenceRepository systemCategoryPreferenceRepository, CurrentUserService currentUserService) {
        this.objectMapper = objectMapper;
        this.categoryMapper = categoryMapper;
        this.categoryRepository = categoryRepository;
        this.systemCategoryRepository = systemCategoryRepository;
        this.systemCategoryPreferenceRepository = systemCategoryPreferenceRepository;
        this.currentUserService = currentUserService;
    }

    public List<SystemCategory> getAllSystemCategories() {
        return systemCategoryRepository.findAll();
    }

    public SystemCategoryPreference getSystemCategoryPreferences(Long userId) {
        return systemCategoryPreferenceRepository.findByUserId(userId);
    }

    public List<Category> getAllUserCategories(Long userId) {
        return categoryRepository.findAllByUserId(userId);
    }

    public List<CategoryDTO> getAll(Jwt jwt) {
        try {
            Long userId = currentUserService.getCurrentUser(jwt).getId();

            SystemCategoryPreference preferences = getSystemCategoryPreferences(userId);

            SystemCategoryPreferenceDetail preferenceDetail = objectMapper.readValue(
                    preferences.getPreferences(),
                    SystemCategoryPreferenceDetail.class
            );

            List<Long> hiddenCategories = preferenceDetail.getHiddenCategories();
            Map<String, String> customColors = preferenceDetail.getCustomColors();

            // Get user active system category and update the color according color preferences
            List<CategoryDTO> systemCategoryList = getAllSystemCategories().stream()
                    .filter(category -> !hiddenCategories.contains(category.getId()) )
                    .map(category -> {
                        CategoryDTO dto =  categoryMapper.toDTO(category);
                        // Change system category color to user-defined color
                        String customColor = customColors.get( String.valueOf(category.getId())  );
                        if (customColor != null) {
                            dto.setColor(customColor);
                        }
                        return dto;
                    })
                    .toList();

            List<CategoryDTO> userCategoryList = getAllUserCategories(userId).stream()
                    .map(CategoryMapper::toDTO).toList();

            return Stream.concat(systemCategoryList.stream(), userCategoryList.stream())
                    .collect(Collectors.toList());

        } catch (JsonProcessingException ex) {
            throw new SystemException(ErrorCode.CAT_PREF_PARSE_ERROR, ex);
        }
    }


}
