package com.studentexpense.tracker.dto;

import com.studentexpense.tracker.entity.enums.ExpenseCategory;
import com.studentexpense.tracker.entity.enums.PaymentMode;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ExpenseRequest {

    @NotBlank(message = "Expense name is required")
    private String name;

    @NotNull
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull
    private PaymentMode paymentMode; // CASH -> deduct Cash wallet, UPI -> deduct Bank wallet

    @NotNull
    private ExpenseCategory category;

    private String note;

    // Optional — if not provided, service layer defaults to today
    private LocalDate date;
}
