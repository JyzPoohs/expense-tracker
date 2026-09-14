package com.expense.tracker.exception;

import com.expense.tracker.constant.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

        return buildResponse(ErrorCode.SYS_BAD_REQUEST.getCode(),
                ErrorCode.SYS_BAD_REQUEST.getDefaultMessage(),
                HttpStatus.BAD_REQUEST,
                req,
                violations);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        log.warn(
                "Malformed JSON request: method={}, uri={}, error={}",
                req.getMethod(),
                req.getRequestURI(),
                ex.getMostSpecificCause().getMessage()
        );

        return buildResponse(ErrorCode.SYS_BAD_REQUEST.getCode(),
                ErrorCode.SYS_BAD_REQUEST.getDefaultMessage(),
                HttpStatus.BAD_REQUEST,
                req, null);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        log.warn("Resource not found error: {}", ex.getMessage());

        return buildResponse(ErrorCode.SYS_NOT_FOUND.getCode(),
                ErrorCode.SYS_NOT_FOUND.getDefaultMessage(),
                HttpStatus.NOT_FOUND,
                req, null);
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex, HttpServletRequest req) {
        log.error("Unhandled exception", ex);

        return buildResponse(ErrorCode.SYS_INTERNAL_ERROR.getCode()
                , ErrorCode.SYS_INTERNAL_ERROR.getDefaultMessage()
                , HttpStatus.INTERNAL_SERVER_ERROR
                , req, null);
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
