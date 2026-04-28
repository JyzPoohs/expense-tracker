package com.expense.tracker.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseException{

    public ResourceNotFoundException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage, HttpStatus.NOT_FOUND);
    }
}
