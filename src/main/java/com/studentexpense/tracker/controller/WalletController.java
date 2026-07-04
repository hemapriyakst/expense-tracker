package com.studentexpense.tracker.controller;

import com.studentexpense.tracker.dto.WalletResponse;
import com.studentexpense.tracker.entity.enums.WalletType;
import com.studentexpense.tracker.service.WalletService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    // GET /api/wallets -> both wallet cards for the dashboard
    @GetMapping
    public List<WalletResponse> getAllWallets() {
        return walletService.getAllWallets();
    }

    // POST /api/wallets/init -> first-launch setup
    @PostMapping("/init")
    @ResponseStatus(HttpStatus.CREATED)
    public List<WalletResponse> initializeWallets(@RequestBody InitRequest request) {
        return walletService.initializeWallets(request.cashBalance(), request.bankBalance());
    }

    // PUT /api/wallets/{type}/thresholds -> customize warning thresholds
    @PutMapping("/{type}/thresholds")
    public WalletResponse updateThresholds(
            @PathVariable WalletType type,
            @RequestBody ThresholdRequest request) {
        return walletService.updateThresholds(type, request.lowThreshold(), request.criticalThreshold());
    }

    // Simple request records — Java 17 records are a clean fit for immutable DTOs
    public record InitRequest(@NotNull BigDecimal cashBalance, @NotNull BigDecimal bankBalance) {}

    public record ThresholdRequest(@NotNull BigDecimal lowThreshold, @NotNull BigDecimal criticalThreshold) {}
}
