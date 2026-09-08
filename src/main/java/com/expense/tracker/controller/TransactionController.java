package com.expense.tracker.controller;

import com.expense.tracker.dto.TransactionDTO;
import com.expense.tracker.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@PreAuthorize("hasRole('USER')")
@CrossOrigin(origins = "http://localhost:5173")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionDTO> create(@AuthenticationPrincipal Jwt jwt, @RequestBody TransactionDTO transactionDTO) {
        return ResponseEntity.ok(transactionService.create(jwt, transactionDTO));
    }

    @GetMapping("/{id}")
    public TransactionDTO getById(@PathVariable Long id) {
        return transactionService.getById(id);
    }

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getAll(@AuthenticationPrincipal Jwt jwt,
                                                       @RequestParam(required = false) String type,
                                                       @RequestParam(required = false) String category,
                                                       @RequestParam(required = false) Integer month,
                                                       @RequestParam(required = false) Integer year) {
        return ResponseEntity.ok(transactionService.getAll(jwt, type, category, month, year));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionDTO> update(@PathVariable Long id, @RequestBody TransactionDTO transactionDTO) {
        return ResponseEntity.ok(transactionService.update(id, transactionDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transactionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<TransactionDTO>> getByType(@PathVariable String type) {
        return ResponseEntity.ok(transactionService.getByType(type));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<TransactionDTO>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(transactionService.getByCategory(category));
    }

}
