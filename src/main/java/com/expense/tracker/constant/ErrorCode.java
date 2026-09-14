package com.expense.tracker.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // SYS: System / Infrastructure
    SYS_BAD_REQUEST        ("SYS-400000", "Malformed or unparseable request body"),
    SYS_NOT_FOUND          ("SYS-404000", "Requested resource not found"),
    SYS_METHOD_NOT_ALLOWED ("SYS-405000", "HTTP method not allowed"),
    SYS_UNSUPPORTED_MEDIA  ("SYS-415000", "Unsupported media type"),
    SYS_INTERNAL_ERROR     ("SYS-500000", "Unexpected internal server error"),

    // TRX: Transaction
    TRX_VALIDATION_ERROR   ("TRX-400001", "Transaction validation failed"),
    TRX_INVALID_TYPE       ("TRX-400002", "Transaction type must be INCOME or EXPENSE"),
    TRX_INVALID_AMOUNT     ("TRX-400003", "Amount must be greater than zero"),
    TRX_DATE_REQUIRED      ("TRX-400004", "Transaction date is required"),
    TRX_NOT_FOUND          ("TRX-404001", "Transaction not found"),
    TRX_DUPLICATE          ("TRX-409001", "Duplicate transaction detected"),
    TRX_PROCESSING_ERROR   ("TRX-500001", "Transaction processing error"),

    // USR: User
    USR_VALIDATION_ERROR   ("USR-400001", "User data validation failed"),
    USR_INVALID_PREFS      ("USR-400002", "User preferences data is invalid"),
    USR_UNAUTHENTICATED    ("USR-401001", "Authentication is required"),
    USR_FORBIDDEN          ("USR-403001", "Access denied — resource belongs to another user"),
    USR_NOT_FOUND          ("USR-404001", "User not found"),
    USR_ALREADY_EXISTS     ("USR-409001", "User account already exists"),
    USR_PROVISIONING_ERROR ("USR-500001", "User provisioning failed"),

    // ATH: Auth / Token
    ATH_INVALID_FORMAT     ("ATH-400001", "Token format is invalid"),
    ATH_TOKEN_EXPIRED      ("ATH-401001", "Token has expired"),
    ATH_TOKEN_INVALID      ("ATH-401002", "Token signature is invalid"),
    ATH_MISSING_HEADER     ("ATH-401003", "Authorization header is missing"),
    ATH_INSUFFICIENT_SCOPE ("ATH-403001", "Token lacks required scope or role"),

    // CAT: Category
    CAT_VALIDATION_ERROR   ("CAT-400001", "Category validation failed"),
    CAT_INVALID_TYPE       ("CAT-400002", "Invalid category type"),
    CAT_NOT_FOUND          ("CAT-404001", "Category not found"),
    CAT_ALREADY_EXISTS     ("CAT-409001", "Category with this name already exists"),
    CAT_PREF_PARSE_ERROR   ("CAT-500001", "Failed to parse category preferences"),

    // BDG: Budget
    BDG_VALIDATION_ERROR   ("BDG-400001", "Budget validation failed"),
    BDG_INVALID_PERIOD     ("BDG-400002", "Invalid budget period"),
    BDG_INVALID_LIMIT      ("BDG-400003", "Budget limit must be positive"),
    BDG_NOT_FOUND          ("BDG-404001", "Budget not found"),
    BDG_ALREADY_EXISTS     ("BDG-409001", "Budget already exists for this category and period"),
    BDG_CALC_ERROR         ("BDG-500001", "Budget calculation error"),

    // DSH: Dashboard / Chart
    DSH_INVALID_DATE_RANGE ("DSH-400001", "Date range is invalid or too wide"),
    DSH_INVALID_CHART_TYPE ("DSH-400002", "Unsupported chart type requested"),
    DSH_AGGREGATION_ERROR  ("DSH-500001", "Error aggregating dashboard data");

    private final String code;
    private final String defaultMessage;
}
