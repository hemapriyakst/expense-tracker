package com.studentexpense.tracker.dto;

import com.studentexpense.tracker.entity.Wallet;
import com.studentexpense.tracker.entity.enums.WalletType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class WalletResponse {
    private Long id;
    private WalletType type;
    private BigDecimal balance;
    private BigDecimal lowThreshold;
    private BigDecimal criticalThreshold;
    private String status; // "HEALTHY" | "LOW" | "CRITICAL" — computed, for the dashboard color

    public static WalletResponse from(Wallet wallet) {
        String status;
        if (wallet.getBalance().compareTo(wallet.getCriticalThreshold()) <= 0) {
            status = "CRITICAL";
        } else if (wallet.getBalance().compareTo(wallet.getLowThreshold()) <= 0) {
            status = "LOW";
        } else {
            status = "HEALTHY";
        }
        return new WalletResponse(
                wallet.getId(),
                wallet.getType(),
                wallet.getBalance(),
                wallet.getLowThreshold(),
                wallet.getCriticalThreshold(),
                status
        );
    }
}
