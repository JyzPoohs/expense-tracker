package com.expense.tracker.exception;

import com.expense.tracker.constant.ErrorCode;
import org.springframework.http.HttpStatus;

public abstract class UnauthorizedException extends ClientException{

    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode, HttpStatus.UNAUTHORIZED);
    }

    public UnauthorizedException(ErrorCode errorCode, String customMessage) {
        super(errorCode, HttpStatus.CONFLICT, customMessage);
    }
}
