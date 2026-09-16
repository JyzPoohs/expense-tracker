package com.expense.tracker.service;

import com.expense.tracker.dto.BudgetDTO;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class BudgetService {
    public BudgetDTO getOverall(Jwt jwt, int month, int year) {
        return BudgetDTO.builder().build();
    }

    public BudgetDTO createOverall(Jwt jwt) {
        return BudgetDTO.builder().build();
    }
}
