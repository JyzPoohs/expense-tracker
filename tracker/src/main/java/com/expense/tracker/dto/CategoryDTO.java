package com.expense.tracker.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;
    private String type;
    private String color;
    private String icon;
    private boolean isActive;
}
