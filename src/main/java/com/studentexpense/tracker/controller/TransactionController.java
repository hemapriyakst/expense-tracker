package com.studentexpense.tracker.controller;

import com.studentexpense.tracker.dto.ExpenseRequest;
import com.studentexpense.tracker.dto.TopUpRequest;
import com.studentexpense.tracker.dto.TransactionResponse;
import com.studentexpense.tracker.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // POST /api/transactions/expense -> add an expense, deducts the right wallet
    @PostMapping("/expense")
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse addExpense(@Valid @RequestBody ExpenseRequest request) {
        return transactionService.createExpense(request);
    }

    // POST /api/transactions/topup -> add money to a wallet
    @PostMapping("/topup")
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse addTopUp(@Valid @RequestBody TopUpRequest request) {
        return transactionService.createTopUp(request);
    }

    // GET /api/transactions/recent -> last 10 for the dashboard
    @GetMapping("/recent")
    public List<TransactionResponse> getRecent() {
        return transactionService.getRecentTransactions(10);
    }

    // GET /api/transactions -> full history (used for charts, filtering, export)
    @GetMapping
    public List<TransactionResponse> getAll() {
        return transactionService.getAllTransactions();
    }

    // DELETE /api/transactions/{id} -> reverses the wallet effect, then deletes
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
    }
}
