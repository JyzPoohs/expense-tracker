package com.expense.tracker.service;

import com.expense.tracker.dto.TransactionDTO;
import com.expense.tracker.entity.Transaction;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {
    private List<Transaction> transactionList = new ArrayList<>();

//    public TransactionDTO getById(Long id) {
//        return transactionList.get(id);
//    }
}
