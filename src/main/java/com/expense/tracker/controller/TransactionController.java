package com.expense.tracker.controller;

import com.expense.tracker.dto.TransactionDTO;
import com.expense.tracker.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@PreAuthorize("hasRole('USER')")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionDTO> create(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody TransactionDTO transactionDTO) {
        return ResponseEntity.ok(transactionService.create(jwt, transactionDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getById(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getById(jwt, id));
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
    public ResponseEntity<TransactionDTO> update(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id, @Valid @RequestBody TransactionDTO transactionDTO) {
        return ResponseEntity.ok(transactionService.update(jwt, id, transactionDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        transactionService.delete(jwt, id);
        return ResponseEntity.noContent().build();
    }
}
