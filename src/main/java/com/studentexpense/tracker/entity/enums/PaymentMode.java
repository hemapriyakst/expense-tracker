package com.studentexpense.tracker.entity.enums;

/**
 * How a transaction was paid/received.
 * This is intentionally a separate enum from WalletType — it's the
 * user-facing concept ("I paid by Cash or UPI"), while WalletType is the
 * internal storage concept (Cash wallet / Bank wallet). The service layer
 * maps PaymentMode -> WalletType:
 *   CASH -> WalletType.CASH
 *   UPI  -> WalletType.BANK
 */
public enum PaymentMode {
    CASH,
    UPI
}
