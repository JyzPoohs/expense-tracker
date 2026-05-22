package com.expense.tracker.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SystemCategoryPreferenceDetail {
    private List<Long> hiddenCategories;
    private Map<String, String> customColors;
}
