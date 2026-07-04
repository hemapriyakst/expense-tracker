package com.studentexpense.tracker.entity;

import com.studentexpense.tracker.entity.enums.ExpenseCategory;
import com.studentexpense.tracker.entity.enums.PaymentMode;
import com.studentexpense.tracker.entity.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * A single ledger entry — either an EXPENSE (money out) or a TOPUP (money in).
 * Every wallet balance change MUST go through a Transaction row, so the
 * balance is always reconstructable/auditable from history.
 */
@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type; // EXPENSE or TOPUP

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMode paymentMode; // CASH or UPI -> tells us which wallet

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    // Name/title of the expense, e.g. "Lunch", "Bus ticket". Optional for TOPUP.
    private String name;

    // Only set when type == EXPENSE
    @Enumerated(EnumType.STRING)
    private ExpenseCategory category;

    // Optional free-text note
    private String note;

    // User-editable date (defaults to today from the frontend)
    @Column(nullable = false)
    private LocalDate date;

    // Server-set, immutable — actual creation timestamp for auditing/sorting
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
