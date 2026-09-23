package com.expense.tracker.exception;

import com.expense.tracker.constant.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import javax.naming.AuthenticationException;
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

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNoResourceFound(NoResourceFoundException ex, HttpServletRequest req) {
        log.warn("No resource found: {}", ex.getResourcePath());

        return buildResponse(
                ErrorCode.SYS_NOT_FOUND.getCode(),
                "The requested endpoint does not exist",
                HttpStatus.NOT_FOUND, req, null);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        log.warn("Request method not support error: {} {}", ex.getMethod(), req.getRequestURL());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ApiErrorResponse.builder()
                        .code(ErrorCode.SYS_METHOD_NOT_ALLOWED.getCode())
                        .message(ErrorCode.SYS_METHOD_NOT_ALLOWED.getDefaultMessage())
                        .status(HttpStatus.METHOD_NOT_ALLOWED.value())
                        .error(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase())
                        .path(req.getRequestURI())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        log.warn("Resource not found error: {}", ex.getMessage());

        return buildResponse(ErrorCode.SYS_NOT_FOUND.getCode(),
                ErrorCode.SYS_NOT_FOUND.getDefaultMessage(),
                HttpStatus.NOT_FOUND,
                req, null);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthorizationDenied(AuthorizationDeniedException ex, HttpServletRequest req) {
        return buildResponse(ErrorCode.USR_FORBIDDEN.getCode(),
                ErrorCode.USR_FORBIDDEN.getDefaultMessage(),
                HttpStatus.FORBIDDEN,
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
