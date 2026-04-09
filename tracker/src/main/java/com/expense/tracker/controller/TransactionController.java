package com.expense.tracker.controller;

import com.expense.tracker.dto.TransactionDTO;
import com.expense.tracker.entity.Transaction;
import com.expense.tracker.service.TransactionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

//    @GetMapping("/{id}")
//    public TransactionDTO getTransactionById(@RequestParam Long id) {
//        return transactionService.getById(id);
//    }
}
