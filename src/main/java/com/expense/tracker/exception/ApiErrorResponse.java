package com.expense.tracker.exception;

import java.time.LocalDateTime;

public record ApiErrorResponse (
        String code,
        String message,
        int status,
        String error,
        String path,
        LocalDateTime timestamp
) {}
