package com.expense.tracker.exception;

import com.expense.tracker.constant.ErrorCode;
import org.springframework.http.HttpStatus;

public class SystemException extends ServerException{
    public SystemException(ErrorCode errorCode) {
        super(errorCode, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    protected SystemException(ErrorCode errorCode, HttpStatus httpStatus, String customMessage) {
        super(errorCode, httpStatus, customMessage);
    }

    public SystemException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, HttpStatus.INTERNAL_SERVER_ERROR,
                errorCode.getDefaultMessage(), cause);
    }

    protected SystemException(ErrorCode errorCode, String customMessage, Throwable cause) {
        super(errorCode, HttpStatus.INTERNAL_SERVER_ERROR, customMessage, cause);
    }
}
