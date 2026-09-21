package com.expense.tracker.controller;

import com.expense.tracker.dto.BudgetDTO;
import com.expense.tracker.service.BudgetService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
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
        return ResponseEntity.ok(budgetService.createOverallBudget(jwt, month, year));
    }

    @GetMapping("/category")
    public ResponseEntity<List<BudgetDTO>> getCategoryBudgets(@AuthenticationPrincipal Jwt jwt, @RequestParam int month, @RequestParam int year) {
        return ResponseEntity.ok(budgetService.getCategoryBudgets(jwt, month, year));
    }

    @PostMapping("/category")
    public ResponseEntity<BudgetDTO> createCategoryBudget(@AuthenticationPrincipal Jwt jwt, @RequestParam Long categoryId, @RequestParam int month, @RequestParam int year) {
        return ResponseEntity.ok(budgetService.createCategoryBudget(jwt, categoryId, month, year));
    }

}
