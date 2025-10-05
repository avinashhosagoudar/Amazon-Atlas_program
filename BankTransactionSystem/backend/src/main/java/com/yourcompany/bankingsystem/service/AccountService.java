package com.yourcompany.bankingsystem.service;

import com.yourcompany.bankingsystem.model.Account;
import com.yourcompany.bankingsystem.model.Customer;
import com.yourcompany.bankingsystem.model.Transaction;
import com.yourcompany.bankingsystem.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AccountService {
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private AuditLogService auditLogService;
    
    @Lazy
    @Autowired
    private TransactionService transactionService;
    
    // Create a new account
    public Account createAccount(String customerId, String accountType, double initialDeposit) {
        // Validate customer exists
        Customer customer = customerService.getCustomerById(customerId);
        if (!customer.isActive()) {
            throw new IllegalArgumentException("Cannot create account for inactive customer");
        }
        
        // Validate account type
        if (!accountType.equals("SB") && !accountType.equals("CA")) {
            throw new IllegalArgumentException("Account type must be SB (Savings) or CA (Current)");
        }
        
        // Check if customer already has an account of this type
        List<Account> existingAccounts = accountRepository.findActiveAccountsByCustomerIdAndAccountType(customerId, accountType);
        if (!existingAccounts.isEmpty()) {
            String accountTypeName = accountType.equals("SB") ? "Savings Bank" : "Current Account";
            throw new IllegalArgumentException("Customer already has an active " + accountTypeName + 
                                             " account. Each customer can have only one " + accountTypeName + " account.");
        }
        
        // Validate initial deposit
        if (initialDeposit <= 0) {
            throw new IllegalArgumentException("Initial deposit must be positive");
        }
        
        // Check minimum balance requirements
        double minBalance = accountType.equals("SB") ? 1000.0 : 5000.0;
        if (initialDeposit < minBalance) {
            throw new IllegalArgumentException("Minimum initial deposit for " + accountType + 
                                             " account is INR " + minBalance);
        }
        
        // Create account
        Account account = new Account(customerId, accountType, initialDeposit);
        Account savedAccount = accountRepository.save(account);
        
        // Log audit
        auditLogService.logAction(customerId, "ACCOUNT_CREATION", "ACCOUNT", savedAccount.getId());
        
        return savedAccount;
    }
    
    // Get account by account number
    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }
    
    // Get all accounts for a customer
    public List<Account> getAccountsByCustomerId(String customerId) {
        return accountRepository.findActiveAccountsByCustomerId(customerId);
    }
    
    // Update account balance (used internally by transaction service)
    @Transactional
    public Account updateBalance(String accountNumber, double newBalance) {
        Account account = getAccountByNumber(accountNumber);
        
        if (!account.isActive()) {
            throw new IllegalArgumentException("Cannot update balance for inactive account");
        }
        
        double oldBalance = account.getBalance();
        account.setBalance(newBalance);
        Account updatedAccount = accountRepository.save(account);
        
        // Log audit
        auditLogService.logAction(account.getCustomerId(), "BALANCE_UPDATE", "ACCOUNT", 
                                 account.getId(), String.valueOf(oldBalance), String.valueOf(newBalance));
        
        return updatedAccount;
    }
    
    // Validate account for transaction
    public boolean validateAccountForTransaction(String accountNumber, double amount, String transactionType) {
        try {
            Account account = getAccountByNumber(accountNumber);
            
            if (!account.isActive()) {
                return false;
            }
            
            // For debits (withdrawals and transfers), check sufficient balance
            if ((transactionType.equals("WITHDRAW") || transactionType.equals("TRANSFER"))) {
        // Check if account has sufficient balance for the transaction
        if (account.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds");
        }                // Check minimum balance requirements after transaction
                double minBalance = account.getAccountType().equals("SB") ? 1000.0 : 5000.0;
                double balanceAfterTransaction = account.getBalance() - amount;
                
                if (balanceAfterTransaction < minBalance) {
                    String accountTypeName = account.getAccountType().equals("SB") ? "Savings Bank" : "Current Account";
                    throw new IllegalArgumentException(
                        String.format("Minimum balance of INR %.2f must be maintained for %s account", 
                                    minBalance, accountTypeName)
                    );
                }
            }
            
            return true;
        } catch (IllegalArgumentException e) {
            // Re-throw validation errors with details
            throw e;
        } catch (Exception e) {
            return false;
        }
    }
    
    // Close account with password verification and auto-withdrawal
    @Transactional
    public Map<String, Object> closeAccount(String accountNumber, String customerId, String password) {
        Account account = getAccountByNumber(accountNumber);
        
        // Verify customer owns the account
        if (!account.getCustomerId().equals(customerId)) {
            throw new IllegalArgumentException("Account does not belong to customer");
        }
        
        // Verify password
        boolean isPasswordValid = customerService.verifyCustomerPassword(customerId, password);
        if (!isPasswordValid) {
            auditLogService.logAction(customerId, "FAILED_ACCOUNT_CLOSURE_AUTH", "ACCOUNT", 
                                     accountNumber, null, "Incorrect password");
            throw new IllegalArgumentException("🔒 Incorrect password. Account closure denied.");
        }
        
        double withdrawnAmount = 0.0;
        String transactionId = null;
        
        // If account has balance, withdraw all money first (bypassing minimum balance check)
        if (account.getBalance() > 0) {
            withdrawnAmount = account.getBalance();
            
            // Use special withdrawal method for account closure that bypasses minimum balance
            Transaction withdrawalTransaction = transactionService.withdrawForAccountClosure(
                accountNumber, customerId, password
            );
            
            transactionId = withdrawalTransaction.getTransactionId();
            
            // Log the auto-withdrawal
            auditLogService.logAction(customerId, "AUTO_WITHDRAW_BEFORE_CLOSURE", "TRANSACTION", 
                                     transactionId, accountNumber, 
                                     String.format("Amount: %.2f", withdrawnAmount));
        }
        
        // Now close the account
        account.setActive(false);
        accountRepository.save(account);
        
        // Log account closure
        auditLogService.logAction(customerId, "ACCOUNT_CLOSURE", "ACCOUNT", account.getId());
        
        // Prepare response
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("accountNumber", accountNumber);
        response.put("withdrawnAmount", withdrawnAmount);
        response.put("transactionId", transactionId);
        response.put("message", withdrawnAmount > 0 
            ? "INR " + String.format("%.2f", withdrawnAmount) + " withdrawn and account closed successfully"
            : "Account closed successfully");
        
        return response;
    }
    
    // Get account statistics
    public long getAccountCountByCustomer(String customerId) {
        return accountRepository.countByCustomerId(customerId);
    }
    
    // Get total balance across all accounts for a customer
    public double getTotalBalanceForCustomer(String customerId) {
        List<Account> accounts = accountRepository.findAccountsForTotalBalance(customerId);
        return accounts.stream()
                      .filter(Account::isActive)
                      .mapToDouble(Account::getBalance)
                      .sum();
    }
}
