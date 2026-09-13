package com.expense.tracker.exception;

import com.expense.tracker.constant.ErrorCode;
import org.springframework.http.HttpStatus;

public abstract class ConflictException extends ClientException{
    public ConflictException(ErrorCode errorCode) {
        super(errorCode, HttpStatus.CONFLICT);
    }

    public ConflictException(ErrorCode errorCode, String customMessage) {
        super(errorCode, HttpStatus.CONFLICT, customMessage);
    }

    public ConflictException(ErrorCode errorCode, HttpStatus httpStatus, String customMessage, Throwable cause) {
        super(errorCode, httpStatus, customMessage, cause);
    }
}
