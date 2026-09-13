package com.expense.tracker.exception;

import com.expense.tracker.constant.ErrorCode;
import org.springframework.http.HttpStatus;

public abstract class ServerException extends AppException {
    public ServerException(ErrorCode errorCode, HttpStatus httpStatus) {
        super(errorCode, httpStatus);
    }

    protected ServerException(ErrorCode errorCode, HttpStatus httpStatus, String customMessage) {
        super(errorCode, httpStatus, customMessage);
    }

    protected ServerException(ErrorCode errorCode, HttpStatus httpStatus, String customMessage, Throwable cause) {
        super(errorCode, httpStatus, customMessage, cause);
    }
}
