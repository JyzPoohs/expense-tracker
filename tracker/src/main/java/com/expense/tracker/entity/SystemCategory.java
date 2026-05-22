package com.expense.tracker.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Builder
@Table(name = "system_categories")
@AllArgsConstructor
public class SystemCategory extends BaseCategory {

}
