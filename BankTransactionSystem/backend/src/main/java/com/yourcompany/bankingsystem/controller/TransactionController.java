package com.yourcompany.bankingsystem.controller;

import com.yourcompany.bankingsystem.model.Transaction;
import com.yourcompany.bankingsystem.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    
    @Autowired
    private TransactionService transactionService;
    
    // Deposit money
    @PostMapping("/deposit")
    public ResponseEntity<?> depositMoney(@RequestBody Map<String, Object> request) {
        try {
            String accountNumber = (String) request.get("accountNumber");
            Double amount = ((Number) request.get("amount")).doubleValue();
            
            Transaction transaction = transactionService.depositMoney(accountNumber, amount);
            
            if (transaction != null && "SUCCESS".equals(transaction.getStatus())) {
                return ResponseEntity.ok(Map.of(
                    "message", "Deposit successful", 
                    "success", true,
                    "transaction", transaction
                ));
            } else {
                return ResponseEntity.badRequest().body(Map.of("message", "Deposit failed", "success", false));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage(), "success", false));
        }
    }
    
    // Withdraw money
    @PostMapping("/withdraw")
    public ResponseEntity<?> withdrawMoney(@RequestBody Map<String, Object> request) {
        try {
            String accountNumber = (String) request.get("accountNumber");
            Double amount = ((Number) request.get("amount")).doubleValue();
            String password = (String) request.get("password");
            
            Transaction transaction = transactionService.withdrawMoneyWithTransaction(accountNumber, amount, password);
            
            if (transaction != null && "SUCCESS".equals(transaction.getStatus())) {
                return ResponseEntity.ok(Map.of(
                    "message", "Withdrawal successful", 
                    "success", true,
                    "transaction", transaction
                ));
            } else {
                return ResponseEntity.badRequest().body(Map.of("message", "Withdrawal failed", "success", false));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage(), "success", false));
        }
    }
    
    // Transfer money
    @PostMapping("/transfer")
    public ResponseEntity<?> transferMoney(@RequestBody Map<String, Object> request) {
        try {
            String fromAccountNumber = (String) request.get("fromAccountNumber");
            String toAccountNumber = (String) request.get("toAccountNumber");
            Double amount = ((Number) request.get("amount")).doubleValue();
            String password = (String) request.get("password");
            
            Transaction transaction = transactionService.transferMoney(fromAccountNumber, toAccountNumber, amount, password);
            
            if (transaction != null && "SUCCESS".equals(transaction.getStatus())) {
                return ResponseEntity.ok(Map.of(
                    "message", "Transfer successful", 
                    "success", true,
                    "transaction", transaction
                ));
            } else {
                return ResponseEntity.badRequest().body(
                    Map.of("message", "Transfer failed", "success", false)
                );
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                Map.of("message", e.getMessage(), "success", false)
            );
        }
    }
    
    // Get transaction history
    @GetMapping("/history/{accountNumber}")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable String accountNumber) {
        try {
            List<Transaction> transactions = transactionService.getTransactionHistory(accountNumber);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    // Get transaction statistics
    @GetMapping("/statistics")
    public ResponseEntity<?> getTransactionStatistics(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDateTime start = LocalDateTime.parse(startDate);
            LocalDateTime end = LocalDateTime.parse(endDate);
            
            TransactionService.TransactionStatistics stats = 
                transactionService.getTransactionStatistics(start, end);
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    
    // Get recent transactions from stack
    @GetMapping("/recent")
    public ResponseEntity<List<Transaction>> getRecentTransactions() {
        try {
            List<Transaction> transactions = transactionService.getRecentTransactionsFromStack();
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    // Get pending transactions from queue
    @GetMapping("/pending")
    public ResponseEntity<List<Transaction>> getPendingTransactions() {
        try {
            List<Transaction> transactions = transactionService.getPendingTransactionsFromQueue();
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    // Process pending transactions
    @PostMapping("/process-pending")
    public ResponseEntity<?> processPendingTransactions() {
        try {
            transactionService.processPendingTransactions();
            return ResponseEntity.ok(Map.of("message", "Pending transactions processed", "success", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage(), "success", false));
        }
    }
    
    // Get transaction limits for account type
    @GetMapping("/limits/{accountType}")
    public ResponseEntity<?> getTransactionLimits(@PathVariable String accountType) {
        try {
            String limitsInfo = transactionService.getTransactionLimits(accountType);
            
            // Parse limits into structured response
            Map<String, Object> limits = new java.util.HashMap<>();
            
            if (accountType.equals("SB")) {
                limits.put("accountType", "Savings Bank");
                limits.put("minAmount", 0.01);
                limits.put("maxAmount", 50000.0);
                limits.put("dailyLimit", 100000.0);
            } else {
                limits.put("accountType", "Current Account");
                limits.put("minAmount", 500.0);
                limits.put("maxAmount", 200000.0);
                limits.put("dailyLimit", 500000.0);
            }
            
            return ResponseEntity.ok(limits);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
