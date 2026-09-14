package com.expense.tracker.exception;

import com.expense.tracker.constant.ErrorCode;
import org.springframework.http.HttpStatus;

public abstract class ClientException extends AppException {
    protected ClientException(ErrorCode errorCode, HttpStatus httpStatus) {
        super(errorCode, httpStatus);
    }

    protected ClientException(ErrorCode errorCode, HttpStatus httpStatus, String customMessage) {
        super(errorCode, httpStatus, customMessage);
    }

    protected ClientException(ErrorCode errorCode, HttpStatus httpStatus, String customMessage, Throwable cause) {
        super(errorCode, httpStatus, customMessage, cause);
    }
}
