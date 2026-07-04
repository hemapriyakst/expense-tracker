package com.studentexpense.tracker.exception;

/**
 * Thrown when an expense would take a wallet balance negative.
 * A RuntimeException (unchecked) since this is a business-rule violation,
 * not a recoverable checked condition the caller must explicitly handle.
 */
public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
