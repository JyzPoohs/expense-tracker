package com.expense.tracker.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(ValidationException ex, HttpServletRequest req){
        log.warn("Validation error: {} - {}", ex.getErrorCode(), ex.getMessage());

        return buildResponse(ex.getErrorCode(), ex.getMessage(), ex.getHttpStatus(), req, ex.getViolations());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<FieldViolation> violations = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new FieldViolation(fe.getField(), fe.getDefaultMessage()))
                .toList();

        log.warn("Request validation failed: {}", violations);

        return buildResponse(ex.getStatusCode().toString(),
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                req,
                violations);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiErrorResponse> handleApp(
            AppException ex,
            HttpServletRequest req
    ) {
        if (ex instanceof ServerException) {
            log.error("Server exception: {}", ex.getErrorCode(), ex);
        } else {
            log.warn("Client exception: {} - {}", ex.getErrorCode(), ex.getMessage());
        }

        return buildResponse(ex.getErrorCode(), ex.getMessage(), ex.getHttpStatus(), req, null);
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(String code,
                                                           String message,
                                                           HttpStatus status,
                                                           HttpServletRequest req,
                                                           List<FieldViolation> violations) {
        ApiErrorResponse body = ApiErrorResponse.builder()
                .code(code)
                .message(message)
                .status(status.value())
                .error(status.getReasonPhrase())
                .path(req.getRequestURI())
                .timestamp(LocalDateTime.now())
                .violations(violations)
                .build();

        return ResponseEntity.status(status).body(body);
    }
}
