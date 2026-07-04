package com.studentexpense.tracker.service;

import com.studentexpense.tracker.dto.ExpenseRequest;
import com.studentexpense.tracker.dto.TopUpRequest;
import com.studentexpense.tracker.dto.TransactionResponse;
import com.studentexpense.tracker.entity.Transaction;
import com.studentexpense.tracker.entity.Wallet;
import com.studentexpense.tracker.entity.enums.PaymentMode;
import com.studentexpense.tracker.entity.enums.TransactionType;
import com.studentexpense.tracker.entity.enums.WalletType;
import com.studentexpense.tracker.exception.InsufficientBalanceException;
import com.studentexpense.tracker.exception.ResourceNotFoundException;
import com.studentexpense.tracker.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletService walletService;

    public TransactionService(TransactionRepository transactionRepository, WalletService walletService) {
        this.transactionRepository = transactionRepository;
        this.walletService = walletService;
    }

    // Maps the user-facing "how did you pay" to the internal wallet it affects
    private WalletType resolveWalletType(PaymentMode mode) {
        return mode == PaymentMode.CASH ? WalletType.CASH : WalletType.BANK;
    }

    /**
     * @Transactional: if the wallet debit succeeds but saving the transaction
     * row fails (or vice versa), BOTH are rolled back. Without this, you could
     * end up with a wallet balance that doesn't match transaction history —
     * exactly the auditability bug we're trying to avoid.
     */
    @Transactional
    public TransactionResponse createExpense(ExpenseRequest request) {
        WalletType walletType = resolveWalletType(request.getPaymentMode());
        Wallet wallet = walletService.getWalletByType(walletType);

        if (wallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException(
                    "Insufficient balance in " + walletType + " wallet. Available: "
                            + wallet.getBalance() + ", required: " + request.getAmount());
        }

        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.EXPENSE);
        transaction.setPaymentMode(request.getPaymentMode());
        transaction.setAmount(request.getAmount());
        transaction.setName(request.getName());
        transaction.setCategory(request.getCategory());
        transaction.setNote(request.getNote());
        transaction.setDate(request.getDate() != null ? request.getDate() : LocalDate.now());

        Transaction saved = transactionRepository.save(transaction);
        walletService.debit(walletType, request.getAmount());

        return TransactionResponse.from(saved);
    }

    @Transactional
    public TransactionResponse createTopUp(TopUpRequest request) {
        WalletType walletType = resolveWalletType(request.getPaymentMode());

        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.TOPUP);
        transaction.setPaymentMode(request.getPaymentMode());
        transaction.setAmount(request.getAmount());
        transaction.setName("Top-up");
        transaction.setNote(request.getNote());
        transaction.setDate(LocalDate.now());

        Transaction saved = transactionRepository.save(transaction);
        walletService.credit(walletType, request.getAmount());

        return TransactionResponse.from(saved);
    }

    public List<TransactionResponse> getRecentTransactions(int limit) {
        return transactionRepository.findAllByOrderByDateDescCreatedAtDesc().stream()
                .limit(limit)
                .map(TransactionResponse::from)
                .toList();
    }

    public List<TransactionResponse> getAllTransactions() {
        return transactionRepository.findAllByOrderByDateDescCreatedAtDesc().stream()
                .map(TransactionResponse::from)
                .toList();
    }

    @Transactional
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found: " + id));

        // Reverse the wallet effect before deleting, so balances stay accurate
        WalletType walletType = resolveWalletType(transaction.getPaymentMode());
        if (transaction.getType() == TransactionType.EXPENSE) {
            walletService.credit(walletType, transaction.getAmount()); // give the money back
        } else {
            walletService.debit(walletType, transaction.getAmount()); // undo the top-up
        }

        transactionRepository.delete(transaction);
    }
}
