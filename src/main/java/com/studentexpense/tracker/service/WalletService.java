package com.studentexpense.tracker.service;

import com.studentexpense.tracker.dto.WalletResponse;
import com.studentexpense.tracker.entity.Wallet;
import com.studentexpense.tracker.entity.enums.WalletType;
import com.studentexpense.tracker.exception.ResourceNotFoundException;
import com.studentexpense.tracker.repository.WalletRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    // Constructor injection (preferred over @Autowired on fields —
    // makes dependencies explicit and the class easier to unit test)
    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public List<WalletResponse> getAllWallets() {
        return walletRepository.findAll().stream()
                .map(WalletResponse::from)
                .toList();
    }

    public Wallet getWalletByType(WalletType type) {
        return walletRepository.findByType(type)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Wallet not set up yet: " + type + ". Call POST /api/wallets/init first."));
    }

    // Called once, on first launch, to create both wallets with starting balances
    public List<WalletResponse> initializeWallets(BigDecimal cashBalance, BigDecimal bankBalance) {
        if (walletRepository.findByType(WalletType.CASH).isPresent()
                || walletRepository.findByType(WalletType.BANK).isPresent()) {
            throw new IllegalStateException("Wallets are already initialized.");
        }

        Wallet cash = new Wallet(null, WalletType.CASH, cashBalance,
                new BigDecimal("200.00"), new BigDecimal("100.00"));
        Wallet bank = new Wallet(null, WalletType.BANK, bankBalance,
                new BigDecimal("500.00"), new BigDecimal("200.00"));

        walletRepository.save(cash);
        walletRepository.save(bank);

        return getAllWallets();
    }

    public WalletResponse updateThresholds(WalletType type, BigDecimal lowThreshold, BigDecimal criticalThreshold) {
        Wallet wallet = getWalletByType(type);
        wallet.setLowThreshold(lowThreshold);
        wallet.setCriticalThreshold(criticalThreshold);
        return WalletResponse.from(walletRepository.save(wallet));
    }

    // Package-private-ish helpers used by TransactionService to actually move money.
    // Kept here so ALL balance mutation logic lives in one place.
    public void credit(WalletType type, BigDecimal amount) {
        Wallet wallet = getWalletByType(type);
        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);
    }

    public void debit(WalletType type, BigDecimal amount) {
        Wallet wallet = getWalletByType(type);
        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);
        // Note: intentionally NOT throwing if balance goes negative here.
        // The check happens in TransactionService BEFORE calling debit(),
        // so this method stays a simple, dumb "move the number" operation.
    }
}
