package com.expense.tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class TransactionDTO {
    private Long id;
    private Long user_id;
    private String note;
    private BigDecimal amount;
    private String type;
    private String category;
    private LocalDateTime date;
    private String remarks;
}
