package com.yourcompany.bankingsystem.service;

import com.yourcompany.bankingsystem.model.Account;
import com.yourcompany.bankingsystem.model.Customer;
import com.yourcompany.bankingsystem.model.Transaction;
import com.yourcompany.bankingsystem.model.custom.TransactionQueue;
import com.yourcompany.bankingsystem.model.custom.TransactionStack;
import com.yourcompany.bankingsystem.model.custom.TransactionHistory;
import com.yourcompany.bankingsystem.repository.TransactionRepository;
import com.yourcompany.bankingsystem.util.LoggingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TransactionService {
    
    // Transaction Limits for Savings Bank (SB) Account
    private static final double SB_MIN_TRANSACTION_AMOUNT = 0.01;       // Minimum: INR 0.01
    private static final double SB_MAX_TRANSACTION_AMOUNT = 50000.0;    // Maximum per transaction: INR 50,000
    private static final double SB_DAILY_LIMIT = 100000.0;              // Daily limit: INR 1,00,000
    
    // Transaction Limits for Current Account (CA)
    private static final double CA_MIN_TRANSACTION_AMOUNT = 500.0;      // Minimum: INR 500
    private static final double CA_MAX_TRANSACTION_AMOUNT = 200000.0;   // Maximum per transaction: INR 2,00,000
    private static final double CA_DAILY_LIMIT = 500000.0;              // Daily limit: INR 5,00,000
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private AuditLogService auditLogService;
    
    @Autowired
    private TransactionQueue transactionQueue;
    
    @Autowired
    private TransactionStack transactionStack;
    
    @Autowired
    private TransactionHistory transactionHistory;
    
    // Validate transaction limits based on account type
    private void validateTransactionLimits(String accountNumber, double amount, String transactionType) {
        Account account = accountService.getAccountByNumber(accountNumber);
        String accountType = account.getAccountType();
        
        double minAmount = accountType.equals("SB") ? SB_MIN_TRANSACTION_AMOUNT : CA_MIN_TRANSACTION_AMOUNT;
        double maxAmount = accountType.equals("SB") ? SB_MAX_TRANSACTION_AMOUNT : CA_MAX_TRANSACTION_AMOUNT;
        double dailyLimit = accountType.equals("SB") ? SB_DAILY_LIMIT : CA_DAILY_LIMIT;
        
        String accountTypeName = accountType.equals("SB") ? "Savings Bank" : "Current Account";
        
        // Check minimum amount
        if (amount < minAmount) {
            throw new IllegalArgumentException(
                String.format("Transaction amount must be at least INR %.2f for %s account", minAmount, accountTypeName)
            );
        }
        
        // Check maximum amount per transaction
        if (amount > maxAmount) {
            throw new IllegalArgumentException(
                String.format("Transaction amount cannot exceed INR %.2f per transaction for %s account", maxAmount, accountTypeName)
            );
        }
        
        // Check daily limit (only for debit transactions: WITHDRAW and TRANSFER)
        if (transactionType.equals("WITHDRAW") || transactionType.equals("TRANSFER")) {
            double todayTotal = calculateTodayDebitTotal(accountNumber);
            
            if (todayTotal + amount > dailyLimit) {
                double remainingLimit = dailyLimit - todayTotal;
                throw new IllegalArgumentException(
                    String.format("Daily transaction limit exceeded for %s account. Daily limit: INR %.2f, Used today: INR %.2f, Remaining: INR %.2f, Attempted: INR %.2f",
                        accountTypeName, dailyLimit, todayTotal, remainingLimit, amount)
                );
            }
        }
    }
    
    // Calculate total debit transactions (WITHDRAW + TRANSFER) for today
    private double calculateTodayDebitTotal(String accountNumber) {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        
        List<Transaction> todayTransactions = transactionRepository
            .findSuccessfulDebitTransactionsByAccountNumberAndDateRange(accountNumber, startOfDay, endOfDay);
        
        return todayTransactions.stream()
            .mapToDouble(Transaction::getAmount)
            .sum();
    }
    
    // Get transaction limits for account type
    public String getTransactionLimits(String accountType) {
        if (accountType.equals("SB")) {
            return String.format(
                "Savings Bank Account Limits:\n" +
                "  - Minimum per transaction: INR %.2f\n" +
                "  - Maximum per transaction: INR %.2f\n" +
                "  - Daily limit: INR %.2f",
                SB_MIN_TRANSACTION_AMOUNT, SB_MAX_TRANSACTION_AMOUNT, SB_DAILY_LIMIT
            );
        } else {
            return String.format(
                "Current Account Limits:\n" +
                "  - Minimum per transaction: INR %.2f\n" +
                "  - Maximum per transaction: INR %.2f\n" +
                "  - Daily limit: INR %.2f",
                CA_MIN_TRANSACTION_AMOUNT, CA_MAX_TRANSACTION_AMOUNT, CA_DAILY_LIMIT
            );
        }
    }
    
    // Deposit money to account
    @Transactional
    public Transaction depositMoney(String accountNumber, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        
        try {
            // Validate transaction limits
            validateTransactionLimits(accountNumber, amount, "DEPOSIT");
            
            // Get current account details
            Account account = accountService.getAccountByNumber(accountNumber);
            
            // Create transaction record
            Transaction transaction = new Transaction(null, accountNumber, amount, "DEPOSIT", 
                                                    "Deposit to account " + accountNumber);
            
            // Add to processing queue
            transactionQueue.enqueue(transaction);
            
            // Process the transaction
            double newBalance = account.getBalance() + amount;
            accountService.updateBalance(accountNumber, newBalance);
            
            // Update transaction status
            transaction.setStatus("SUCCESS");
            transaction.setBalanceAfter(newBalance);
            Transaction savedTransaction = transactionRepository.save(transaction);
            
            // Add to transaction history stack
            transactionStack.push(savedTransaction);
            
            // Add to undo history for reversal capability
            transactionHistory.pushToUndoStack(savedTransaction);
            
            // Log audit with detailed information
            auditLogService.logAction(account.getCustomerId(), "DEPOSIT", "TRANSACTION", 
                                     savedTransaction.getId());
            auditLogService.logAction(account.getCustomerId(), "TRANSACTION_UNDOABLE", "TRANSACTION",
                                     savedTransaction.getTransactionId(), 
                                     null, 
                                     String.format("Amount: %.2f, New Balance: %.2f", amount, newBalance));
            
            return savedTransaction;
        } catch (IllegalArgumentException e) {
            // Handle validation errors with specific messages (limit validation)
            Transaction failedTransaction = new Transaction(null, accountNumber, amount, "DEPOSIT", 
                                                          "Failed deposit: " + e.getMessage());
            failedTransaction.setStatus("FAILED");
            transactionRepository.save(failedTransaction);
            
            auditLogService.logAction("SYSTEM", "FAILED_DEPOSIT", "TRANSACTION", accountNumber);
            throw e; // Re-throw to show user the specific error
        } catch (Exception e) {
            // Handle other unexpected errors
            Transaction failedTransaction = new Transaction(null, accountNumber, amount, "DEPOSIT", 
                                                          "Failed deposit: " + e.getMessage());
            failedTransaction.setStatus("FAILED");
            transactionRepository.save(failedTransaction);
            
            auditLogService.logAction("SYSTEM", "FAILED_DEPOSIT", "TRANSACTION", accountNumber);
            throw new RuntimeException("Deposit failed: " + e.getMessage(), e);
        }
    }
    
    // Withdraw money from account
    @Transactional
    public boolean withdrawMoney(String accountNumber, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        
        try {
            // Validate transaction limits first
            validateTransactionLimits(accountNumber, amount, "WITHDRAW");
            
            // Validate account and balance
            if (!accountService.validateAccountForTransaction(accountNumber, amount, "WITHDRAW")) {
                return false;
            }
            
            // Get current account details
            Account account = accountService.getAccountByNumber(accountNumber);
            
            // Create transaction record
            Transaction transaction = new Transaction(accountNumber, null, amount, "WITHDRAW", 
                                                    "Withdrawal from account " + accountNumber);
            
            // Add to processing queue
            transactionQueue.enqueue(transaction);
            
            // Process the transaction
            double newBalance = account.getBalance() - amount;
            accountService.updateBalance(accountNumber, newBalance);
            
            // Update transaction status
            transaction.setStatus("SUCCESS");
            transaction.setBalanceAfter(newBalance);
            Transaction savedTransaction = transactionRepository.save(transaction);
            
            // Add to transaction history stack
            transactionStack.push(savedTransaction);
            
            // Add to undo history for reversal capability
            transactionHistory.pushToUndoStack(savedTransaction);
            
            // Log audit with detailed information
            auditLogService.logAction(account.getCustomerId(), "WITHDRAW", "TRANSACTION", 
                                     savedTransaction.getId());
            auditLogService.logAction(account.getCustomerId(), "TRANSACTION_UNDOABLE", "TRANSACTION",
                                     savedTransaction.getTransactionId(),
                                     null,
                                     String.format("Amount: %.2f, New Balance: %.2f", amount, newBalance));
            
            return true;
        } catch (IllegalArgumentException e) {
            // Handle validation errors with specific messages
            Transaction failedTransaction = new Transaction(accountNumber, null, amount, "WITHDRAW", 
                                                          "Failed withdrawal: " + e.getMessage());
            failedTransaction.setStatus("FAILED");
            transactionRepository.save(failedTransaction);
            
            auditLogService.logAction("SYSTEM", "FAILED_WITHDRAW", "TRANSACTION", accountNumber);
            throw e; // Re-throw to show user the specific error
        } catch (Exception e) {
            // Handle other errors
            Transaction failedTransaction = new Transaction(accountNumber, null, amount, "WITHDRAW", 
                                                          "Failed withdrawal: " + e.getMessage());
            failedTransaction.setStatus("FAILED");
            transactionRepository.save(failedTransaction);
            
            auditLogService.logAction("SYSTEM", "FAILED_WITHDRAW", "TRANSACTION", accountNumber);
            return false;
        }
    }
    
    // Withdraw money from account and return transaction object
    @Transactional
    public Transaction withdrawMoneyWithTransaction(String accountNumber, double amount, String password) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        
        try {
            // Validate password first
            Account account = accountService.getAccountByNumber(accountNumber);
            boolean isPasswordValid = customerService.verifyCustomerPassword(account.getCustomerId(), password);
            
            if (!isPasswordValid) {
                auditLogService.logAction(account.getCustomerId(), "FAILED_WITHDRAW_AUTH", "TRANSACTION", 
                                         accountNumber, null, "Incorrect password");
                throw new IllegalArgumentException("🔒 Incorrect password. Transaction denied.");
            }
            
            // Validate transaction limits first
            validateTransactionLimits(accountNumber, amount, "WITHDRAW");
            
            // Validate account and balance
            if (!accountService.validateAccountForTransaction(accountNumber, amount, "WITHDRAW")) {
                throw new IllegalArgumentException("Insufficient balance or invalid account");
            }
            
            // Create transaction record
            Transaction transaction = new Transaction(accountNumber, null, amount, "WITHDRAW", 
                                                    "Withdrawal from account " + accountNumber);
            
            // Add to processing queue
            transactionQueue.enqueue(transaction);
            
            // Process the transaction
            double newBalance = account.getBalance() - amount;
            accountService.updateBalance(accountNumber, newBalance);
            
            // Update transaction status
            transaction.setStatus("SUCCESS");
            transaction.setBalanceAfter(newBalance);
            Transaction savedTransaction = transactionRepository.save(transaction);
            
            // Add to transaction history stack
            transactionStack.push(savedTransaction);
            
            // Add to undo history for reversal capability
            transactionHistory.pushToUndoStack(savedTransaction);
            
            // Log audit with detailed information
            auditLogService.logAction(account.getCustomerId(), "WITHDRAW", "TRANSACTION", 
                                     savedTransaction.getId());
            auditLogService.logAction(account.getCustomerId(), "TRANSACTION_UNDOABLE", "TRANSACTION",
                                     savedTransaction.getTransactionId(),
                                     null,
                                     String.format("Amount: %.2f, New Balance: %.2f", amount, newBalance));
            
            return savedTransaction;
        } catch (IllegalArgumentException e) {
            // Handle validation errors with specific messages
            Transaction failedTransaction = new Transaction(accountNumber, null, amount, "WITHDRAW", 
                                                          "Failed withdrawal: " + e.getMessage());
            failedTransaction.setStatus("FAILED");
            transactionRepository.save(failedTransaction);
            
            auditLogService.logAction("SYSTEM", "FAILED_WITHDRAW", "TRANSACTION", accountNumber);
            throw e; // Re-throw to show user the specific error
        } catch (Exception e) {
            // Handle other errors
            Transaction failedTransaction = new Transaction(accountNumber, null, amount, "WITHDRAW", 
                                                          "Failed withdrawal: " + e.getMessage());
            failedTransaction.setStatus("FAILED");
            transactionRepository.save(failedTransaction);
            
            auditLogService.logAction("SYSTEM", "FAILED_WITHDRAW", "TRANSACTION", accountNumber);
            throw new RuntimeException("Withdrawal failed: " + e.getMessage());
        }
    }
    
    // Special withdrawal for account closure - bypasses minimum balance requirement
    @Transactional
    public Transaction withdrawForAccountClosure(String accountNumber, String customerId, String password) {
        try {
            // Get account details
            Account account = accountService.getAccountByNumber(accountNumber);
            double amount = account.getBalance();
            
            if (amount <= 0) {
                throw new IllegalArgumentException("Account has no balance to withdraw");
            }
            
            // Verify customer owns the account
            if (!account.getCustomerId().equals(customerId)) {
                throw new IllegalArgumentException("Account does not belong to customer");
            }
            
            // Verify password
            boolean isPasswordValid = customerService.verifyCustomerPassword(customerId, password);
            if (!isPasswordValid) {
                auditLogService.logAction(customerId, "FAILED_CLOSURE_WITHDRAW_AUTH", "TRANSACTION", 
                                         accountNumber, null, "Incorrect password");
                throw new IllegalArgumentException("🔒 Incorrect password. Transaction denied.");
            }
            
            // Create transaction record for full withdrawal (account closure)
            Transaction transaction = new Transaction(accountNumber, null, amount, "WITHDRAW", 
                                                    "Full withdrawal for account closure");
            
            // Add to processing queue
            transactionQueue.enqueue(transaction);
            
            // Process the transaction - set balance to 0
            accountService.updateBalance(accountNumber, 0.0);
            
            // Update transaction status
            transaction.setStatus("SUCCESS");
            transaction.setBalanceAfter(0.0);
            Transaction savedTransaction = transactionRepository.save(transaction);
            
            // Add to transaction history stack
            transactionStack.push(savedTransaction);
            
            // Add to undo history for reversal capability
            transactionHistory.pushToUndoStack(savedTransaction);
            
            // Log audit with detailed information
            auditLogService.logAction(customerId, "ACCOUNT_CLOSURE_WITHDRAWAL", "TRANSACTION", 
                                     savedTransaction.getId());
            auditLogService.logAction(customerId, "TRANSACTION_UNDOABLE", "TRANSACTION",
                                     savedTransaction.getTransactionId(),
                                     null,
                                     String.format("Full withdrawal for closure - Amount: %.2f, New Balance: 0.00", amount));
            
            return savedTransaction;
        } catch (IllegalArgumentException e) {
            // Handle validation errors with specific messages
            throw e; // Re-throw to show user the specific error
        } catch (Exception e) {
            // Handle other errors
            throw new RuntimeException("Account closure withdrawal failed: " + e.getMessage());
        }
    }
    
    // Transfer money between accounts
    @Transactional
    public Transaction transferMoney(String fromAccountNumber, String toAccountNumber, double amount, String password) {
        try {
            // Validate password first
            Account fromAccount = accountService.getAccountByNumber(fromAccountNumber);
            boolean isPasswordValid = customerService.verifyCustomerPassword(fromAccount.getCustomerId(), password);
            
            if (!isPasswordValid) {
                auditLogService.logAction(fromAccount.getCustomerId(), "FAILED_TRANSFER_AUTH", "TRANSACTION", 
                                         fromAccountNumber, null, "Incorrect password");
                throw new IllegalArgumentException("🔒 Incorrect password. Transaction denied.");
            }
            
            // Validate transaction limits for from account
            validateTransactionLimits(fromAccountNumber, amount, "TRANSFER");
            
            // Validate from account
            if (!accountService.validateAccountForTransaction(fromAccountNumber, amount, "TRANSFER")) {
                Transaction failedTransaction = new Transaction(fromAccountNumber, toAccountNumber, amount, "TRANSFER", 
                                                              "Transfer validation failed");
                failedTransaction.setStatus("FAILED");
                transactionRepository.save(failedTransaction);
                throw new IllegalArgumentException("Transfer validation failed");
            }
            
            // Get both accounts
            Account toAccount = accountService.getAccountByNumber(toAccountNumber);
            
            // Create transaction
            Transaction transaction = new Transaction(fromAccountNumber, toAccountNumber, amount, "TRANSFER", 
                                                    "Transfer from " + fromAccountNumber + " to " + toAccountNumber);
            
            // Debit from source account
            double newFromBalance = fromAccount.getBalance() - amount;
            accountService.updateBalance(fromAccountNumber, newFromBalance);
            
            // Credit to destination account
            double newToBalance = toAccount.getBalance() + amount;
            accountService.updateBalance(toAccountNumber, newToBalance);
            
            // Update transaction status
            transaction.setStatus("SUCCESS");
            transaction.setBalanceAfter(newFromBalance);
            Transaction savedTransaction = transactionRepository.save(transaction);
            
            // Push to transaction stack for undo functionality
            transactionStack.push(savedTransaction);
            
            // Log transaction
            auditLogService.logAction(fromAccount.getCustomerId(), "MONEY_TRANSFER", "TRANSACTION", 
                                     savedTransaction.getTransactionId());
            LoggingUtil.logTransaction(savedTransaction.getTransactionId(), fromAccountNumber, 
                                      "TRANSFER", amount, "SUCCESS");
            
            return savedTransaction;
            
        } catch (IllegalArgumentException e) {
            // Handle validation errors with specific messages
            Transaction failedTransaction = new Transaction(fromAccountNumber, toAccountNumber, amount, "TRANSFER", 
                                                          "Failed transfer: " + e.getMessage());
            failedTransaction.setStatus("FAILED");
            transactionRepository.save(failedTransaction);
            
            auditLogService.logAction("SYSTEM", "FAILED_TRANSFER", "TRANSACTION", 
                                     fromAccountNumber + "->" + toAccountNumber);
            throw e; // Re-throw to show user the specific error
        } catch (Exception e) {
            // Handle other unexpected errors
            Transaction failedTransaction = new Transaction(fromAccountNumber, toAccountNumber, amount, "TRANSFER", 
                                                          "Transfer failed: " + e.getMessage());
            failedTransaction.setStatus("FAILED");
            transactionRepository.save(failedTransaction);
            
            auditLogService.logAction("SYSTEM", "FAILED_TRANSFER", "TRANSACTION", 
                                     fromAccountNumber + "->" + toAccountNumber);
            LoggingUtil.logError("TransactionService", "transferMoney", 
                               "Transfer failed: " + e.getMessage(), e);
            throw new RuntimeException("Transfer failed: " + e.getMessage(), e);
        }
    }
    
    // Internal transfer method for system operations (redo, processing) - no password required
    @Transactional
    private Transaction transferMoneyInternal(String fromAccountNumber, String toAccountNumber, double amount) {
        try {
            // Validate transaction limits for from account
            validateTransactionLimits(fromAccountNumber, amount, "TRANSFER");
            
            // Validate from account
            if (!accountService.validateAccountForTransaction(fromAccountNumber, amount, "TRANSFER")) {
                Transaction failedTransaction = new Transaction(fromAccountNumber, toAccountNumber, amount, "TRANSFER", 
                                                              "Transfer validation failed");
                failedTransaction.setStatus("FAILED");
                transactionRepository.save(failedTransaction);
                throw new IllegalArgumentException("Transfer validation failed");
            }
            
            // Get both accounts
            Account fromAccount = accountService.getAccountByNumber(fromAccountNumber);
            Account toAccount = accountService.getAccountByNumber(toAccountNumber);
            
            // Create transaction
            Transaction transaction = new Transaction(fromAccountNumber, toAccountNumber, amount, "TRANSFER", 
                                                    "Transfer from " + fromAccountNumber + " to " + toAccountNumber);
            
            // Debit from source account
            double newFromBalance = fromAccount.getBalance() - amount;
            accountService.updateBalance(fromAccountNumber, newFromBalance);
            
            // Credit to destination account
            double newToBalance = toAccount.getBalance() + amount;
            accountService.updateBalance(toAccountNumber, newToBalance);
            
            // Update transaction status
            transaction.setStatus("SUCCESS");
            transaction.setBalanceAfter(newFromBalance);
            Transaction savedTransaction = transactionRepository.save(transaction);
            
            // Push to transaction stack for undo functionality
            transactionStack.push(savedTransaction);
            
            // Log transaction
            auditLogService.logAction(fromAccount.getCustomerId(), "MONEY_TRANSFER", "TRANSACTION", 
                                     savedTransaction.getTransactionId());
            LoggingUtil.logTransaction(savedTransaction.getTransactionId(), fromAccountNumber, 
                                      "TRANSFER", amount, "SUCCESS");
            
            return savedTransaction;
            
        } catch (IllegalArgumentException e) {
            // Handle validation errors with specific messages
            Transaction failedTransaction = new Transaction(fromAccountNumber, toAccountNumber, amount, "TRANSFER", 
                                                          "Failed transfer: " + e.getMessage());
            failedTransaction.setStatus("FAILED");
            transactionRepository.save(failedTransaction);
            
            auditLogService.logAction("SYSTEM", "FAILED_TRANSFER", "TRANSACTION", 
                                     fromAccountNumber + "->" + toAccountNumber);
            throw e; // Re-throw to show user the specific error
        } catch (Exception e) {
            // Handle other unexpected errors
            Transaction failedTransaction = new Transaction(fromAccountNumber, toAccountNumber, amount, "TRANSFER", 
                                                          "Transfer failed: " + e.getMessage());
            failedTransaction.setStatus("FAILED");
            transactionRepository.save(failedTransaction);
            
            auditLogService.logAction("SYSTEM", "FAILED_TRANSFER", "TRANSACTION", 
                                     fromAccountNumber + "->" + toAccountNumber);
            LoggingUtil.logError("TransactionService", "transferMoneyInternal", 
                               "Transfer failed: " + e.getMessage(), e);
            throw new RuntimeException("Transfer failed: " + e.getMessage(), e);
        }
    }
    
    // Undo last transaction
    @Transactional
    public boolean undoLastTransaction() {
        Transaction lastTransaction = transactionHistory.popFromUndoStack();
        
        if (lastTransaction == null) {
            throw new IllegalStateException("No transaction to undo");
        }
        
        try {
            String transactionType = lastTransaction.getTransactionType();
            
            // Reverse the transaction based on type
            switch (transactionType) {
                case "DEPOSIT":
                    // Reverse deposit = withdraw the amount
                    reverseDeposit(lastTransaction);
                    break;
                    
                case "WITHDRAW":
                    // Reverse withdrawal = deposit the amount back
                    reverseWithdrawal(lastTransaction);
                    break;
                    
                case "TRANSFER":
                    // Reverse transfer = transfer back
                    reverseTransfer(lastTransaction);
                    break;
                    
                default:
                    throw new IllegalArgumentException("Cannot undo transaction type: " + transactionType);
            }
            
            // Mark original transaction as undone
            lastTransaction.setStatus("UNDONE");
            lastTransaction.setDescription(lastTransaction.getDescription() + " [UNDONE]");
            transactionRepository.save(lastTransaction);
            
            // Log undo action
            auditLogService.logAction("SYSTEM", "TRANSACTION_UNDO", "TRANSACTION",
                                     lastTransaction.getTransactionId(),
                                     "SUCCESS", "UNDONE",
                                     null, null);
            
            return true;
            
        } catch (Exception e) {
            // If undo fails, push transaction back to undo stack
            transactionHistory.pushToUndoStack(lastTransaction);
            
            auditLogService.logAction("SYSTEM", "TRANSACTION_UNDO_FAILED", "TRANSACTION",
                                     lastTransaction.getTransactionId(),
                                     null, e.getMessage());
            throw new RuntimeException("Failed to undo transaction: " + e.getMessage(), e);
        }
    }
    
    // Redo last undone transaction
    @Transactional
    public boolean redoLastTransaction() {
        Transaction lastUndoneTransaction = transactionHistory.popFromRedoStack();
        
        if (lastUndoneTransaction == null) {
            throw new IllegalStateException("No transaction to redo");
        }
        
        try {
            String transactionType = lastUndoneTransaction.getTransactionType();
            
            // Re-execute the transaction
            switch (transactionType) {
                case "DEPOSIT":
                    redoDeposit(lastUndoneTransaction);
                    break;
                    
                case "WITHDRAW":
                    redoWithdrawal(lastUndoneTransaction);
                    break;
                    
                case "TRANSFER":
                    redoTransfer(lastUndoneTransaction);
                    break;
                    
                default:
                    throw new IllegalArgumentException("Cannot redo transaction type: " + transactionType);
            }
            
            // Log redo action
            auditLogService.logAction("SYSTEM", "TRANSACTION_REDO", "TRANSACTION",
                                     lastUndoneTransaction.getTransactionId(),
                                     "UNDONE", "SUCCESS");
            
            return true;
            
        } catch (Exception e) {
            // If redo fails, push back to redo stack
            transactionHistory.pushToUndoStack(lastUndoneTransaction);
            
            auditLogService.logAction("SYSTEM", "TRANSACTION_REDO_FAILED", "TRANSACTION",
                                     lastUndoneTransaction.getTransactionId(),
                                     null, e.getMessage());
            throw new RuntimeException("Failed to redo transaction: " + e.getMessage(), e);
        }
    }
    
    // Reverse deposit (withdraw the deposited amount)
    private void reverseDeposit(Transaction originalTransaction) {
        String accountNumber = originalTransaction.getToAccountNumber();
        double amount = originalTransaction.getAmount();
        
        Account account = accountService.getAccountByNumber(accountNumber);
        double newBalance = account.getBalance() - amount;
        
        if (newBalance < 0) {
            throw new IllegalStateException("Cannot undo deposit: insufficient balance");
        }
        
        accountService.updateBalance(accountNumber, newBalance);
        
        // Create reverse transaction record
        Transaction reverseTransaction = new Transaction(accountNumber, null, amount, "DEPOSIT_REVERSAL",
                                                        "Reversal of deposit transaction " + originalTransaction.getTransactionId());
        reverseTransaction.setStatus("SUCCESS");
        reverseTransaction.setBalanceAfter(newBalance);
        transactionRepository.save(reverseTransaction);
        
        auditLogService.logAction(account.getCustomerId(), "DEPOSIT_REVERSAL", "TRANSACTION",
                                 reverseTransaction.getTransactionId());
    }
    
    // Reverse withdrawal (deposit the withdrawn amount back)
    private void reverseWithdrawal(Transaction originalTransaction) {
        String accountNumber = originalTransaction.getFromAccountNumber();
        double amount = originalTransaction.getAmount();
        
        Account account = accountService.getAccountByNumber(accountNumber);
        double newBalance = account.getBalance() + amount;
        
        accountService.updateBalance(accountNumber, newBalance);
        
        // Create reverse transaction record
        Transaction reverseTransaction = new Transaction(null, accountNumber, amount, "WITHDRAW_REVERSAL",
                                                        "Reversal of withdrawal transaction " + originalTransaction.getTransactionId());
        reverseTransaction.setStatus("SUCCESS");
        reverseTransaction.setBalanceAfter(newBalance);
        transactionRepository.save(reverseTransaction);
        
        auditLogService.logAction(account.getCustomerId(), "WITHDRAW_REVERSAL", "TRANSACTION",
                                 reverseTransaction.getTransactionId());
    }
    
    // Reverse transfer (transfer back from to-account to from-account)
    private void reverseTransfer(Transaction originalTransaction) {
        String fromAccountNumber = originalTransaction.getFromAccountNumber();
        String toAccountNumber = originalTransaction.getToAccountNumber();
        double amount = originalTransaction.getAmount();
        
        // Transfer back: to -> from
        Account fromAccount = accountService.getAccountByNumber(fromAccountNumber);
        Account toAccount = accountService.getAccountByNumber(toAccountNumber);
        
        if (toAccount.getBalance() < amount) {
            throw new IllegalStateException("Cannot undo transfer: recipient account has insufficient balance");
        }
        
        double fromNewBalance = fromAccount.getBalance() + amount;
        double toNewBalance = toAccount.getBalance() - amount;
        
        accountService.updateBalance(fromAccountNumber, fromNewBalance);
        accountService.updateBalance(toAccountNumber, toNewBalance);
        
        // Create reverse transaction record
        Transaction reverseTransaction = new Transaction(toAccountNumber, fromAccountNumber, amount, "TRANSFER_REVERSAL",
                                                        "Reversal of transfer transaction " + originalTransaction.getTransactionId());
        reverseTransaction.setStatus("SUCCESS");
        reverseTransaction.setBalanceAfter(toNewBalance);
        transactionRepository.save(reverseTransaction);
        
        auditLogService.logAction(fromAccount.getCustomerId(), "TRANSFER_REVERSAL_IN", "TRANSACTION",
                                 reverseTransaction.getTransactionId());
        auditLogService.logAction(toAccount.getCustomerId(), "TRANSFER_REVERSAL_OUT", "TRANSACTION",
                                 reverseTransaction.getTransactionId());
    }
    
    // Redo deposit
    private void redoDeposit(Transaction originalTransaction) {
        String accountNumber = originalTransaction.getToAccountNumber();
        double amount = originalTransaction.getAmount();
        
        depositMoney(accountNumber, amount);
    }
    
    // Redo withdrawal
    private void redoWithdrawal(Transaction originalTransaction) {
        String accountNumber = originalTransaction.getFromAccountNumber();
        double amount = originalTransaction.getAmount();
        
        withdrawMoney(accountNumber, amount);
    }
    
    // Redo transfer (internal operation, no password required)
    private void redoTransfer(Transaction originalTransaction) {
        String fromAccountNumber = originalTransaction.getFromAccountNumber();
        String toAccountNumber = originalTransaction.getToAccountNumber();
        double amount = originalTransaction.getAmount();
        
        transferMoneyInternal(fromAccountNumber, toAccountNumber, amount);
    }
    
    // Get transaction history for an account
    public List<Transaction> getTransactionHistory(String accountNumber) {
        Sort sort = Sort.by(Sort.Direction.DESC, "transactionDate");
        return transactionRepository.findByAccountNumber(accountNumber, sort);
    }
    
    // Get recent transactions using stack (LIFO)
    public List<Transaction> getRecentTransactionsFromStack() {
        return transactionStack.getAllTransactions();
    }
    
    // Get pending transactions using queue (FIFO)
    public List<Transaction> getPendingTransactionsFromQueue() {
        return transactionQueue.getAllTransactions();
    }
    
    // Process pending transactions from queue
    @Transactional
    public void processPendingTransactions() {
        while (!transactionQueue.isEmpty()) {
            Transaction transaction = transactionQueue.dequeue();
            if (transaction != null && "PENDING".equals(transaction.getStatus())) {
                // Reprocess the transaction
                processTransaction(transaction);
            }
        }
    }
    
    // Get transaction statistics
    public TransactionStatistics getTransactionStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        Sort sort = Sort.by(Sort.Direction.DESC, "transactionDate");
        List<Transaction> transactions = transactionRepository.findByTransactionDateBetween(startDate, endDate, sort);
        
        long totalTransactions = transactions.size();
        long successfulTransactions = transactions.stream()
                                                 .mapToLong(t -> "SUCCESS".equals(t.getStatus()) ? 1 : 0)
                                                 .sum();
        long failedTransactions = transactions.stream()
                                            .mapToLong(t -> "FAILED".equals(t.getStatus()) ? 1 : 0)
                                            .sum();
        double totalAmount = transactions.stream()
                                       .filter(t -> "SUCCESS".equals(t.getStatus()))
                                       .mapToDouble(Transaction::getAmount)
                                       .sum();
        
        return new TransactionStatistics(totalTransactions, successfulTransactions, 
                                       failedTransactions, totalAmount);
    }
    
    // Helper method to process individual transaction
    private void processTransaction(Transaction transaction) {
        try {
            String type = transaction.getTransactionType();
            switch (type) {
                case "DEPOSIT":
                    depositMoney(transaction.getToAccountNumber(), transaction.getAmount());
                    break;
                case "WITHDRAW":
                    withdrawMoney(transaction.getFromAccountNumber(), transaction.getAmount());
                    break;
                case "TRANSFER":
                    transferMoneyInternal(transaction.getFromAccountNumber(), 
                                transaction.getToAccountNumber(), transaction.getAmount());
                    break;
            }
        } catch (Exception e) {
            transaction.setStatus("FAILED");
            transaction.setDescription("Processing failed: " + e.getMessage());
            transactionRepository.save(transaction);
        }
    }
    
    // Inner class for transaction statistics
    public static class TransactionStatistics {
        private long totalTransactions;
        private long successfulTransactions;
        private long failedTransactions;
        private double totalAmount;
        
        public TransactionStatistics(long totalTransactions, long successfulTransactions, 
                                   long failedTransactions, double totalAmount) {
            this.totalTransactions = totalTransactions;
            this.successfulTransactions = successfulTransactions;
            this.failedTransactions = failedTransactions;
            this.totalAmount = totalAmount;
        }
        
        // Getters
        public long getTotalTransactions() { return totalTransactions; }
        public long getSuccessfulTransactions() { return successfulTransactions; }
        public long getFailedTransactions() { return failedTransactions; }
        public double getTotalAmount() { return totalAmount; }
    }
    
    // Inner class for transaction history summary
    public static class TransactionHistorySummary {
        private int undoStackSize;
        private int redoStackSize;
        private boolean canUndo;
        private boolean canRedo;
        
        public TransactionHistorySummary(int undoStackSize, int redoStackSize, boolean canUndo, boolean canRedo) {
            this.undoStackSize = undoStackSize;
            this.redoStackSize = redoStackSize;
            this.canUndo = canUndo;
            this.canRedo = canRedo;
        }
        
        public int getUndoStackSize() { return undoStackSize; }
        public int getRedoStackSize() { return redoStackSize; }
        public boolean isCanUndo() { return canUndo; }
        public boolean isCanRedo() { return canRedo; }
    }
    
    // Check if undo is available
    public boolean canUndo() {
        return transactionHistory.canUndo();
    }
    
    // Check if redo is available
    public boolean canRedo() {
        return transactionHistory.canRedo();
    }
    
    // Get undoable transactions
    public List<Transaction> getUndoableTransactions() {
        return transactionHistory.getUndoableTransactions();
    }
    
    // Get undoable transactions for specific account
    public List<Transaction> getUndoableTransactionsForAccount(String accountNumber) {
        List<Transaction> allTransactions = transactionHistory.getUndoableTransactions();
        return allTransactions.stream()
            .filter(txn -> accountNumber.equals(txn.getFromAccountNumber()) || 
                          accountNumber.equals(txn.getToAccountNumber()))
            .collect(java.util.stream.Collectors.toList());
    }
    
    // Get redoable transactions
    public List<Transaction> getRedoableTransactions() {
        return transactionHistory.getRedoableTransactions();
    }
    
    // Get redoable transactions for specific account
    public List<Transaction> getRedoableTransactionsForAccount(String accountNumber) {
        List<Transaction> allTransactions = transactionHistory.getRedoableTransactions();
        return allTransactions.stream()
            .filter(txn -> accountNumber.equals(txn.getFromAccountNumber()) || 
                          accountNumber.equals(txn.getToAccountNumber()))
            .collect(java.util.stream.Collectors.toList());
    }
    
    // Undo last transaction for specific account
    @Transactional
    public boolean undoLastTransactionForAccount(String accountNumber) {
        // Get all undoable transactions
        List<Transaction> undoableTransactions = transactionHistory.getUndoableTransactions();
        
        // Find the last transaction for this account
        Transaction targetTransaction = null;
        for (int i = undoableTransactions.size() - 1; i >= 0; i--) {
            Transaction txn = undoableTransactions.get(i);
            if (accountNumber.equals(txn.getFromAccountNumber()) || 
                accountNumber.equals(txn.getToAccountNumber())) {
                targetTransaction = txn;
                break;
            }
        }
        
        if (targetTransaction == null) {
            throw new IllegalStateException("No transaction to undo for account " + accountNumber);
        }
        
        // Remove the transaction from undo stack
        Transaction poppedTransaction = transactionHistory.popFromUndoStack();
        
        // If it's not the target transaction, we need to handle it differently
        // For now, we'll just undo the last transaction of this account we found
        // This is a simplified version - you might want to implement a more sophisticated approach
        
        try {
            String transactionType = targetTransaction.getTransactionType();
            
            // Reverse the transaction based on type
            switch (transactionType) {
                case "DEPOSIT":
                    reverseDeposit(targetTransaction);
                    break;
                    
                case "WITHDRAW":
                    reverseWithdrawal(targetTransaction);
                    break;
                    
                case "TRANSFER":
                    reverseTransfer(targetTransaction);
                    break;
                    
                default:
                    throw new IllegalArgumentException("Cannot undo transaction type: " + transactionType);
            }
            
            // Mark original transaction as undone
            targetTransaction.setStatus("UNDONE");
            targetTransaction.setDescription(targetTransaction.getDescription() + " [UNDONE]");
            transactionRepository.save(targetTransaction);
            
            // Log undo action
            auditLogService.logAction("SYSTEM", "TRANSACTION_UNDO", "TRANSACTION",
                                     targetTransaction.getTransactionId(),
                                     "SUCCESS", "UNDONE",
                                     null, null);
            
            return true;
            
        } catch (Exception e) {
            // If undo fails, push transaction back to undo stack
            transactionHistory.pushToUndoStack(poppedTransaction);
            
            auditLogService.logAction("SYSTEM", "TRANSACTION_UNDO_FAILED", "TRANSACTION",
                                     targetTransaction.getTransactionId(),
                                     null, e.getMessage());
            throw new RuntimeException("Failed to undo transaction: " + e.getMessage(), e);
        }
    }
    
    // Redo last transaction for specific account
    @Transactional
    public boolean redoLastTransactionForAccount(String accountNumber) {
        // Get all redoable transactions
        List<Transaction> redoableTransactions = transactionHistory.getRedoableTransactions();
        
        // Find the last undone transaction for this account
        Transaction targetTransaction = null;
        for (int i = redoableTransactions.size() - 1; i >= 0; i--) {
            Transaction txn = redoableTransactions.get(i);
            if (accountNumber.equals(txn.getFromAccountNumber()) || 
                accountNumber.equals(txn.getToAccountNumber())) {
                targetTransaction = txn;
                break;
            }
        }
        
        if (targetTransaction == null) {
            throw new IllegalStateException("No transaction to redo for account " + accountNumber);
        }
        
        // Remove the transaction from redo stack
        Transaction poppedTransaction = transactionHistory.popFromRedoStack();
        
        try {
            String transactionType = targetTransaction.getTransactionType();
            
            // Re-execute the transaction
            switch (transactionType) {
                case "DEPOSIT":
                    redoDeposit(targetTransaction);
                    break;
                    
                case "WITHDRAW":
                    redoWithdrawal(targetTransaction);
                    break;
                    
                case "TRANSFER":
                    redoTransfer(targetTransaction);
                    break;
                    
                default:
                    throw new IllegalArgumentException("Cannot redo transaction type: " + transactionType);
            }
            
            // Log redo action
            auditLogService.logAction("SYSTEM", "TRANSACTION_REDO", "TRANSACTION",
                                     targetTransaction.getTransactionId(),
                                     "UNDONE", "SUCCESS");
            
            return true;
            
        } catch (Exception e) {
            // If redo fails, push back to redo stack
            transactionHistory.pushToUndoStack(poppedTransaction);
            
            auditLogService.logAction("SYSTEM", "TRANSACTION_REDO_FAILED", "TRANSACTION",
                                     targetTransaction.getTransactionId(),
                                     null, e.getMessage());
            throw new RuntimeException("Failed to redo transaction: " + e.getMessage(), e);
        }
    }
    
    // Get transaction history summary
    public TransactionHistorySummary getTransactionHistorySummary() {
        return new TransactionHistorySummary(
            transactionHistory.getUndoStackSize(),
            transactionHistory.getRedoStackSize(),
            transactionHistory.canUndo(),
            transactionHistory.canRedo()
        );
    }
}
