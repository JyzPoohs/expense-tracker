package com.expense.tracker.exception;

import com.expense.tracker.constant.ErrorCode;
import org.springframework.http.HttpStatus;

public class PreferenceParseException extends BaseException {

    public PreferenceParseException(String errorMessage) {
        super(ErrorCode.PREFERENCE_PARSE_ERROR, errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
