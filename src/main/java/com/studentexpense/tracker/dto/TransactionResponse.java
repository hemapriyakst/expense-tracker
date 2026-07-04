package com.studentexpense.tracker.dto;

import com.studentexpense.tracker.entity.Transaction;
import com.studentexpense.tracker.entity.enums.ExpenseCategory;
import com.studentexpense.tracker.entity.enums.PaymentMode;
import com.studentexpense.tracker.entity.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class TransactionResponse {
    private Long id;
    private TransactionType type;
    private PaymentMode paymentMode;
    private BigDecimal amount;
    private String name;
    private ExpenseCategory category;
    private String note;
    private LocalDate date;

    public static TransactionResponse from(Transaction t) {
        return new TransactionResponse(
                t.getId(),
                t.getType(),
                t.getPaymentMode(),
                t.getAmount(),
                t.getName(),
                t.getCategory(),
                t.getNote(),
                t.getDate()
        );
    }
}
