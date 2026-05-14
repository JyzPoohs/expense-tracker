package com.expense.tracker.controller;

import com.expense.tracker.dto.TransactionDTO;
import com.expense.tracker.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:5173")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionDTO> create(@RequestBody TransactionDTO transactionDTO) {
        return ResponseEntity.ok(transactionService.create(transactionDTO));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public TransactionDTO getById(@PathVariable Long id) {
        return transactionService.getById(id);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getAll() {
        return ResponseEntity.ok(transactionService.getAll());
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}")
    public ResponseEntity<TransactionDTO> update(@PathVariable Long id, @RequestBody TransactionDTO transactionDTO) {
        return ResponseEntity.ok(transactionService.update(id, transactionDTO));
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transactionService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/type/{type}")
    public ResponseEntity<List<TransactionDTO>> getByType(@PathVariable String type) {
        return ResponseEntity.ok(transactionService.getByType(type));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/category/{category}")
    public ResponseEntity<List<TransactionDTO>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(transactionService.getByCategory(category));
    }

}
