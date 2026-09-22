package com.expense.tracker.controller;

import com.expense.tracker.dto.BudgetDTO;
import com.expense.tracker.dto.BudgetUpdateRequest;
import com.expense.tracker.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@PreAuthorize("hasRole('USER')")
public class BudgetController {
    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping("/overall")
    public ResponseEntity<BudgetDTO> getOverallBudget(@AuthenticationPrincipal Jwt jwt, @RequestParam int month, @RequestParam int year) {
        return ResponseEntity.ok(budgetService.getOverallBudget(jwt, month, year));
    }

    @PostMapping("/overall")
    public ResponseEntity<BudgetDTO> createOverallBudget(@AuthenticationPrincipal Jwt jwt, @RequestParam int month, @RequestParam int year) {
        return ResponseEntity.status(HttpStatus.CREATED).body(budgetService.createOverallBudget(jwt, month, year));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<BudgetDTO>> getCategoryBudgets(@AuthenticationPrincipal Jwt jwt, @RequestParam int month, @RequestParam int year) {
        return ResponseEntity.ok(budgetService.getCategoryBudgets(jwt, month, year));
    }

    @PostMapping("/categories")
    public ResponseEntity<BudgetDTO> createCategoryBudget(@AuthenticationPrincipal Jwt jwt, @RequestParam Long categoryId, @RequestParam int month, @RequestParam int year) {
        return ResponseEntity.status(HttpStatus.CREATED).body(budgetService.createCategoryBudget(jwt, categoryId, month, year));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetDTO> update(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id, @RequestBody @Valid BudgetUpdateRequest budgetDTO) {
        return ResponseEntity.ok(budgetService.update(jwt, id, budgetDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        budgetService.delete(jwt, id);
        return ResponseEntity.noContent().build();
    }



}
