package com.expense.tracker.controller;

import com.expense.tracker.dto.BudgetDTO;
import com.expense.tracker.service.BudgetService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {
    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping("/overall")
    public ResponseEntity<BudgetDTO> getOverall(@AuthenticationPrincipal Jwt jwt, @RequestParam int month, @RequestParam int year) {
        return ResponseEntity.ok(budgetService.getOverall(jwt, month, year));
    }

    @PostMapping("/overall")
    public ResponseEntity<BudgetDTO> createOverall(@AuthenticationPrincipal Jwt jwt, @RequestParam int month, @RequestParam int year) {
        return ResponseEntity.ok(budgetService.createOverall(jwt, month, year));
    }

}
