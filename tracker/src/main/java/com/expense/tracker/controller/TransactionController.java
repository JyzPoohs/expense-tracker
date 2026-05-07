package com.expense.tracker.controller;

import com.expense.tracker.dto.TransactionDTO;
import com.expense.tracker.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionDTO> create(@RequestBody TransactionDTO transactionDTO) {
        return ResponseEntity.ok(transactionService.create(transactionDTO));
    }

    @GetMapping("/{id}")
    public TransactionDTO getById(@PathVariable Long id) {
        return transactionService.getById(id);
    }

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getAll() {
        return ResponseEntity.ok(transactionService.getAll());
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
}
