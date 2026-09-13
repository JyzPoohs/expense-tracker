package com.expense.tracker.exception;

import com.expense.tracker.constant.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class ValidationException extends ClientException{
    private final List<FieldViolation> violations;

    // Single field violation
    public ValidationException(ErrorCode errorCode, String field, String message) {
        super(errorCode, HttpStatus.BAD_REQUEST);
        this.violations = List.of(new FieldViolation(field, message));
    }

    // Multiple field violations
    public ValidationException(ErrorCode errorCode, List<FieldViolation> violations) {
        super(errorCode, HttpStatus.BAD_REQUEST);
        this.violations = violations == null ? List.of() : List.copyOf(violations);
    }

    // No field violation, only error message
    public ValidationException(ErrorCode errorCode, String customMessage) {
        super(errorCode, HttpStatus.BAD_REQUEST, customMessage);
        this.violations = List.of();
    }

    public ValidationException(ErrorCode errorCode, HttpStatus httpStatus, String customMessage, Throwable cause, List<FieldViolation> violations) {
        super(errorCode, httpStatus, customMessage, cause);
        this.violations = violations;
    }
}
