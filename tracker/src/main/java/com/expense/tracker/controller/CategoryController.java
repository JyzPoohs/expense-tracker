package com.expense.tracker.controller;

import com.expense.tracker.dto.CategoryDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
//    @GetMapping("/{id}")
//    public ResponseEntity<List<CategoryDTO>> getAll(@PathVariable Long id) {
//        return ResponseEntity.ok(List);
//    }
}
