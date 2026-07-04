package com.studentexpense.tracker.dto;

import com.studentexpense.tracker.entity.enums.PaymentMode;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Request body for "top up a wallet" — e.g. received cash from home,
 * or a bank transfer came in.
 */
@Data
public class TopUpRequest {

    @NotNull
    private PaymentMode paymentMode; // CASH -> tops up Cash wallet, UPI -> tops up Bank wallet

    @NotNull
    @DecimalMin(value = "0.01", message = "Top-up amount must be greater than 0")
    private BigDecimal amount;

    private String note;
}
