package com.studentexpense.tracker.entity;

import com.studentexpense.tracker.entity.enums.WalletType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Represents one of the two wallets (CASH or BANK).
 * There will only ever be exactly 2 rows in this table for a single-user app:
 * one WalletType.CASH and one WalletType.BANK.
 */
@Entity
@Table(name = "wallets")
@Data                 // Lombok: generates getters, setters, toString, equals/hashCode
@NoArgsConstructor     // JPA requires a no-arg constructor
@AllArgsConstructor
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)   // store "CASH"/"BANK" as readable text, not 0/1
    @Column(nullable = false, unique = true)
    private WalletType type;

    // BigDecimal, never float/double, for currency to avoid rounding errors
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal balance;

    // Below this -> dashboard shows YELLOW
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal lowThreshold;

    // Below this -> dashboard shows RED
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal criticalThreshold;
}
