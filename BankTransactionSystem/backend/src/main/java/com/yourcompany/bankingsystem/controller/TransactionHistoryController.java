package com.yourcompany.bankingsystem.controller;

import com.yourcompany.bankingsystem.model.Transaction;
import com.yourcompany.bankingsystem.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transaction-history")
@CrossOrigin(origins = "*")
public class TransactionHistoryController {
    
    @Autowired
    private TransactionService transactionService;
    
    // Undo last transaction
    @PostMapping("/undo")
    public ResponseEntity<?> undoLastTransaction() {
        try {
            boolean success = transactionService.undoLastTransaction();
            return ResponseEntity.ok(Map.of(
                "success", success,
                "message", "Transaction undone successfully"
            ));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Failed to undo transaction: " + e.getMessage()
            ));
        }
    }
    
    // Undo last transaction for specific account
    @PostMapping("/undo/{accountNumber}")
    public ResponseEntity<?> undoLastTransactionForAccount(@PathVariable String accountNumber) {
        try {
            boolean success = transactionService.undoLastTransactionForAccount(accountNumber);
            return ResponseEntity.ok(Map.of(
                "success", success,
                "message", "Transaction undone successfully for account " + accountNumber
            ));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Failed to undo transaction: " + e.getMessage()
            ));
        }
    }
    
    // Redo last undone transaction
    @PostMapping("/redo")
    public ResponseEntity<?> redoLastTransaction() {
        try {
            boolean success = transactionService.redoLastTransaction();
            return ResponseEntity.ok(Map.of(
                "success", success,
                "message", "Transaction redone successfully"
            ));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Failed to redo transaction: " + e.getMessage()
            ));
        }
    }
    
    // Redo last undone transaction for specific account
    @PostMapping("/redo/{accountNumber}")
    public ResponseEntity<?> redoLastTransactionForAccount(@PathVariable String accountNumber) {
        try {
            boolean success = transactionService.redoLastTransactionForAccount(accountNumber);
            return ResponseEntity.ok(Map.of(
                "success", success,
                "message", "Transaction redone successfully for account " + accountNumber
            ));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Failed to redo transaction: " + e.getMessage()
            ));
        }
    }
    
    // Check if undo is available
    @GetMapping("/can-undo")
    public ResponseEntity<?> canUndo() {
        boolean canUndo = transactionService.canUndo();
        return ResponseEntity.ok(Map.of("canUndo", canUndo));
    }
    
    // Check if redo is available
    @GetMapping("/can-redo")
    public ResponseEntity<?> canRedo() {
        boolean canRedo = transactionService.canRedo();
        return ResponseEntity.ok(Map.of("canRedo", canRedo));
    }
    
    // Get undoable transactions
    @GetMapping("/undoable")
    public ResponseEntity<List<Transaction>> getUndoableTransactions() {
        try {
            List<Transaction> transactions = transactionService.getUndoableTransactions();
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    // Get undoable transactions for specific account
    @GetMapping("/undoable/{accountNumber}")
    public ResponseEntity<List<Transaction>> getUndoableTransactionsForAccount(@PathVariable String accountNumber) {
        try {
            List<Transaction> transactions = transactionService.getUndoableTransactionsForAccount(accountNumber);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    // Get redoable transactions
    @GetMapping("/redoable")
    public ResponseEntity<List<Transaction>> getRedoableTransactions() {
        try {
            List<Transaction> transactions = transactionService.getRedoableTransactions();
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    // Get redoable transactions for specific account
    @GetMapping("/redoable/{accountNumber}")
    public ResponseEntity<List<Transaction>> getRedoableTransactionsForAccount(@PathVariable String accountNumber) {
        try {
            List<Transaction> transactions = transactionService.getRedoableTransactionsForAccount(accountNumber);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    // Get transaction history summary
    @GetMapping("/summary")
    public ResponseEntity<?> getTransactionHistorySummary() {
        try {
            TransactionService.TransactionHistorySummary summary = transactionService.getTransactionHistorySummary();
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
