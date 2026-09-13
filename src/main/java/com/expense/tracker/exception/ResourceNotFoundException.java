package com.expense.tracker.exception;

import com.expense.tracker.constant.ErrorCode;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ClientException {

    public ResourceNotFoundException(ErrorCode errorCode) {
        super(errorCode, HttpStatus.NOT_FOUND);
    }

    public ResourceNotFoundException(ErrorCode errorCode, String customMessage) {
        super(errorCode, HttpStatus.NOT_FOUND, customMessage);
    }

    public ResourceNotFoundException(ErrorCode errorCode, HttpStatus httpStatus, String customMessage, Throwable cause) {
        super(errorCode, httpStatus, customMessage, cause);
    }
}
