package com.expense.tracker.exception;

public class BaseException extends RuntimeException{
    protected final String errorCode;

    public BaseException(String errorCode, String errorMessage) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
