package com.expense.tracker.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiErrorResponse (
        String code,
        String message,
        int status,
        String error,
        String path,
        LocalDateTime timestamp,
        List<FieldViolation> violations
) {}
