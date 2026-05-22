package com.expense.tracker.mapper;

import com.expense.tracker.dto.CategoryDTO;
import com.expense.tracker.entity.BaseCategory;
import com.expense.tracker.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public static CategoryDTO toDTO(BaseCategory category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .type(category.getType())
                .color(category.getColor())
                .icon(category.getIcon())
                .isActive(category.isActive())
                .build();
    }

//    public Category toEntity(CreateCategoryRequest request) {
//        return Category.builder()
//                .name(request.getName())
//                .type(request.getType())
//                .color(request.getColor())
//                .icon(request.getIcon())
//                .build();
//    }
}
