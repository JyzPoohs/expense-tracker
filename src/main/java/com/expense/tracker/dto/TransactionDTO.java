package com.expense.tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private Long userId;
    @NotBlank(message = "Note must not be blank")
    private String note;
    @NotNull
    private BigDecimal amount;
    @NotBlank
    private String type;
    @NotBlank
    private String category;
    @NotNull
    private LocalDateTime date;
    private String remarks;
}
