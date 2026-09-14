package com.expense.tracker.exception;

import com.expense.tracker.constant.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class AppException extends RuntimeException{
    protected final String errorCode;
    private final HttpStatus httpStatus;

    protected AppException(ErrorCode errorCode, HttpStatus httpStatus) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode.getCode();
        this.httpStatus = httpStatus;
    }

    protected AppException(ErrorCode errorCode, HttpStatus httpStatus, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode.getCode();
        this.httpStatus = httpStatus;
    }

    protected AppException(ErrorCode errorCode, HttpStatus httpStatus, String customMessage, Throwable cause) {
        super(customMessage, cause);
        this.errorCode = errorCode.getCode();
        this.httpStatus = httpStatus;
    }
}
