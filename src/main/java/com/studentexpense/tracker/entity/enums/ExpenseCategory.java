package com.studentexpense.tracker.entity.enums;

/**
 * Fixed category set from the product spec.
 * Only relevant when TransactionType == EXPENSE (null for TOPUP).
 */
public enum ExpenseCategory {
    FOOD,
    TRANSPORT,
    SNACKS,
    STATIONERY,
    ENTERTAINMENT,
    LAUNDRY,
    MEDICAL,
    MISCELLANEOUS
}
