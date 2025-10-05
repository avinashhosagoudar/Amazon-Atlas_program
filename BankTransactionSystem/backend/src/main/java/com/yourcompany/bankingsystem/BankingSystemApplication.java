package com.yourcompany.bankingsystem;

import com.yourcompany.bankingsystem.service.CustomerService;
import com.yourcompany.bankingsystem.service.AccountService;
import com.yourcompany.bankingsystem.service.TransactionService;
import com.yourcompany.bankingsystem.service.AuditLogService;
import com.yourcompany.bankingsystem.model.Customer;
import com.yourcompany.bankingsystem.model.Account;
import com.yourcompany.bankingsystem.model.Transaction;
import com.yourcompany.bankingsystem.model.AuditLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.InputMismatchException;

@SpringBootApplication
@EnableMongoAuditing
public class BankingSystemApplication implements CommandLineRunner {

    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private TransactionService transactionService;
    
    @Autowired
    private AuditLogService auditLogService;

    public static void main(String[] args) {
        SpringApplication.run(BankingSystemApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        
        System.out.println("=== Welcome to Automated Banking System ===");
        
        while (running) {
            try {
                showMainMenu();
                int choice = getIntInput(scanner, "Enter your choice: ");
                
                switch (choice) {
                    case 1:
                        registerCustomer(scanner);
                        break;
                    case 2:
                        customerLogin(scanner);
                        break;
                    case 3:
                        employeeLogin(scanner);
                        break;
                    case 4:
                        running = false;
                        System.out.println("Thank you for using Banking System!");
                        break;
                    default:
                        System.out.println("❌ Invalid option. Please select a valid option (1-4).");
                }
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
                System.out.println("Please try again with valid input.");
                scanner.nextLine(); // Clear the buffer
            }
        }
        scanner.close();
    }
    
    // Helper method to safely read integer input
    private int getIntInput(Scanner scanner, String prompt) {
        while (true) {
            try {
                if (prompt != null && !prompt.isEmpty()) {
                    System.out.print(prompt);
                }
                int value = scanner.nextInt();
                scanner.nextLine(); // consume newline
                return value;
            } catch (InputMismatchException e) {
                System.out.println("❌ Invalid input! Please enter a valid number.");
                scanner.nextLine(); // Clear the invalid input
                if (prompt != null && !prompt.isEmpty()) {
                    System.out.print(prompt);
                }
            }
        }
    }
    
    // Helper method to safely read double input
    private double getDoubleInput(Scanner scanner, String prompt) {
        while (true) {
            try {
                if (prompt != null && !prompt.isEmpty()) {
                    System.out.print(prompt);
                }
                double value = scanner.nextDouble();
                scanner.nextLine(); // consume newline
                return value;
            } catch (InputMismatchException e) {
                System.out.println("❌ Invalid input! Please enter a valid number.");
                scanner.nextLine(); // Clear the invalid input
                if (prompt != null && !prompt.isEmpty()) {
                    System.out.print(prompt);
                }
            }
        }
    }
    
    private void showMainMenu() {
        System.out.println("\n=== Main Menu ===");
        System.out.println("1. Customer Register");
        System.out.println("2. Customer Login");
        System.out.println("3. Employee Login");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }
    
    private void registerCustomer(Scanner scanner) {
        System.out.println("\n=== Customer Registration ===");
        System.out.print("Enter your full Name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        
        System.out.print("Choose your password: ");
        String password = scanner.nextLine();
        
        System.out.print("Choose your password again: ");
        String confirmPassword = scanner.nextLine();
        
        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match!");
            return;
        }
        
        try {
            Customer customer = customerService.registerCustomer(name, email, password);
            System.out.println("Customer registered successfully! Customer ID: " + customer.getId());
            System.out.println("Thank you for registering!");
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }
    
    private void customerLogin(Scanner scanner) {
        System.out.println("\n=== Customer Login ===");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        try {
            Customer customer = customerService.loginCustomer(email, password);
            if (customer != null) {
                System.out.println("Login successful! Welcome, " + customer.getName());
                customerMenu(scanner, customer);
            } else {
                System.out.println("Invalid credentials!");
            }
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }
    
    private void customerMenu(Scanner scanner, Customer customer) {
        boolean loggedIn = true;
        
        while (loggedIn) {
            try {
                System.out.println("\n=== Customer Menu ===");
                System.out.println("1. Create Account");
                System.out.println("2. View Balance");
                System.out.println("3. Deposit Money");
                System.out.println("4. Withdraw Money");
                System.out.println("5. Transfer Money");
                System.out.println("6. Transaction History");
                System.out.println("7. Close Account");
                System.out.println("8. Change Password");
                System.out.println("9. Undo Last Transaction");
                System.out.println("10. Redo Transaction");
                System.out.println("11. View Undo/Redo History");
                System.out.println("12. View Audit Logs");
                System.out.println("13. Exit");
                
                int choice = getIntInput(scanner, "Enter your choice: ");
                
                switch (choice) {
                    case 1:
                        createAccount(scanner, customer);
                        break;
                    case 2:
                        viewBalance(scanner, customer);
                        break;
                    case 3:
                        depositMoney(scanner, customer);
                        break;
                    case 4:
                        withdrawMoney(scanner, customer);
                        break;
                    case 5:
                        transferMoney(scanner, customer);
                        break;
                    case 6:
                        viewTransactionHistory(scanner, customer);
                        break;
                    case 7:
                        closeAccount(scanner, customer);
                        break;
                    case 8:
                        changePassword(scanner, customer);
                        break;
                    case 9:
                        undoLastTransaction(scanner, customer);
                        break;
                    case 10:
                        redoTransaction(scanner, customer);
                        break;
                    case 11:
                        viewUndoRedoHistory(scanner, customer);
                        break;
                    case 12:
                        viewAuditLogs(scanner, customer);
                        break;
                    case 13:
                        loggedIn = false;
                        System.out.println("Logged out successfully!");
                        break;
                    default:
                        System.out.println("❌ Invalid option. Please select a valid option (1-13).");
                }
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
                System.out.println("Please try again with valid input.");
                scanner.nextLine(); // Clear the buffer
            }
        }
    }
    
    private void viewTransactionHistory(Scanner scanner, Customer customer) {
        try {
            List<Account> accounts = accountService.getAccountsByCustomerId(customer.getId());
            if (accounts.isEmpty()) {
                System.out.println("No accounts found!");
                return;
            }
            
            System.out.println("\n=== Transaction History ===");
            for (int i = 0; i < accounts.size(); i++) {
                Account acc = accounts.get(i);
                System.out.println((i + 1) + ". Ac No: " + acc.getAccountNumber() + 
                                 " - Type: " + acc.getAccountType());
            }
            
            System.out.print("Select account number for transaction history: ");
            String accountNumber = scanner.nextLine();
            
            List<Transaction> transactions = transactionService.getTransactionHistory(accountNumber);
            
            if (transactions.isEmpty()) {
                System.out.println("No transactions found for this account.");
                return;
            }
            
            System.out.println("\n=== Transaction History for Account: " + accountNumber + " ===");
            System.out.printf("%-15s %-12s %-10s %-15s %-10s %-20s%n", 
                            "Transaction ID", "Type", "Amount", "Status", "Balance", "Date");
            System.out.println("=".repeat(95));
            
            for (Transaction txn : transactions) {
                System.out.printf("%-15s %-12s %-10.2f %-15s %-10.2f %-20s%n",
                                txn.getTransactionId(),
                                txn.getTransactionType(),
                                txn.getAmount(),
                                txn.getStatus(),
                                txn.getBalanceAfter(),
                                txn.getTransactionDate());
            }
        } catch (Exception e) {
            System.out.println("Error retrieving transaction history: " + e.getMessage());
        }
    }
    
    private void employeeLogin(Scanner scanner) {
        System.out.println("\n=== Employee Login ===");
        System.out.println("Employee login feature coming soon!");
    }
    
    private void createAccount(Scanner scanner, Customer customer) {
        System.out.println("\n=== Create Account ===");
        
        // Show existing accounts
        try {
            List<Account> existingAccounts = accountService.getAccountsByCustomerId(customer.getId());
            if (!existingAccounts.isEmpty()) {
                System.out.println("\nYour Existing Accounts:");
                boolean hasSB = false;
                boolean hasCA = false;
                for (Account acc : existingAccounts) {
                    System.out.println("- " + (acc.getAccountType().equals("SB") ? "Savings Bank" : "Current Account") + 
                                     " (" + acc.getAccountNumber() + ") - Balance: INR " + acc.getBalance());
                    if (acc.getAccountType().equals("SB")) hasSB = true;
                    if (acc.getAccountType().equals("CA")) hasCA = true;
                }
                
                // Check if customer already has both types
                if (hasSB && hasCA) {
                    System.out.println("\n⚠️  You already have both account types (SB and CA).");
                    System.out.println("Each customer can have only ONE Savings Bank and ONE Current Account.");
                    System.out.println("No more accounts can be created.");
                    return;
                }
                
                // Show available account types
                System.out.println("\nAvailable Account Types:");
                if (!hasSB) {
                    System.out.println("- SB (Savings Bank) - Minimum deposit: INR 1,000");
                }
                if (!hasCA) {
                    System.out.println("- CA (Current Account) - Minimum deposit: INR 5,000");
                }
            } else {
                System.out.println("You don't have any accounts yet.");
                System.out.println("\nAccount Types Available:");
                System.out.println("- SB (Savings Bank) - Minimum deposit: INR 1,000");
                System.out.println("- CA (Current Account) - Minimum deposit: INR 5,000");
            }
            System.out.println("\nℹ️  Note: Each customer can have only ONE SB and ONE CA account.");
        } catch (Exception e) {
            // Continue with account creation if error occurs
        }
        
        try {
            System.out.print("\nEnter type of account (SB/CA): ");
            String accountType = scanner.nextLine().toUpperCase();
            
            if (!accountType.equals("SB") && !accountType.equals("CA")) {
                System.out.println("❌ Invalid account type! Only SB or CA allowed.");
                return;
            }
            
            double initialDeposit = getDoubleInput(scanner, "Enter initial deposit: ");
            
            if (initialDeposit <= 0) {
                System.out.println("❌ Initial deposit must be positive!");
                return;
            }
            
            Account account = accountService.createAccount(customer.getId(), accountType, initialDeposit);
            System.out.println("✓ Account created successfully!");
            System.out.println("Account Number: " + account.getAccountNumber());
            System.out.println("Account Type: " + account.getAccountType());
            System.out.println("Initial Balance: INR " + account.getBalance());
        } catch (Exception e) {
            System.out.println("❌ Account creation failed: " + e.getMessage());
        }
    }
    
    private void viewBalance(Scanner scanner, Customer customer) {
        try {
            List<Account> accounts = accountService.getAccountsByCustomerId(customer.getId());
            if (accounts.isEmpty()) {
                System.out.println("No accounts found!");
                return;
            }
            
            System.out.println("\n=== Your Accounts ===");
            for (int i = 0; i < accounts.size(); i++) {
                Account acc = accounts.get(i);
                System.out.println((i + 1) + ". Ac No: " + acc.getAccountNumber() + 
                                 " - Type: " + (acc.getAccountType().equals("SB") ? "Savings Bank" : "Current Account"));
            }
            
            System.out.print("\nSelect account number to view balance: ");
            String accountNumber = scanner.nextLine();
            
            Account selectedAccount = accounts.stream()
                .filter(acc -> acc.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElse(null);
                
            if (selectedAccount == null) {
                System.out.println("❌ Account not found!");
                return;
            }
            
            // Password authentication for viewing balance
            System.out.print("🔒 Enter your password to view balance: ");
            String password = scanner.nextLine();
            
            // Verify password
            Customer verifiedCustomer = customerService.loginCustomer(customer.getEmail(), password);
            if (verifiedCustomer == null) {
                System.out.println("❌ Incorrect password! Access denied.");
                System.out.println("Balance viewing failed due to authentication error.");
                return;
            }
            
            // Password verified, show balance
            System.out.println("\n✓ Authentication successful!");
            System.out.println("=".repeat(50));
            System.out.println("Balance Details for Account: " + accountNumber);
            System.out.println("Account Type: " + (selectedAccount.getAccountType().equals("SB") ? "Savings Bank" : "Current Account"));
            System.out.println("Current Balance: INR " + String.format("%.2f", selectedAccount.getBalance()));
            System.out.println("=".repeat(50));
            
        } catch (Exception e) {
            System.out.println("❌ Error retrieving balance: " + e.getMessage());
        }
    }
    
    private void withdrawMoney(Scanner scanner, Customer customer) {
        try {
            List<Account> accounts = accountService.getAccountsByCustomerId(customer.getId());
            if (accounts.isEmpty()) {
                System.out.println("No accounts found!");
                return;
            }
            
            System.out.println("\n=== Withdraw Money ===");
            for (Account acc : accounts) {
                System.out.println("Ac No: " + acc.getAccountNumber() + 
                                 " - Type: " + acc.getAccountType() +
                                 " - Balance: INR " + acc.getBalance());
            }
            
            System.out.print("Enter account number: ");
            String accountNumber = scanner.nextLine();
            
            Account selectedAccount = accounts.stream()
                .filter(acc -> acc.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElse(null);
                
            if (selectedAccount == null) {
                System.out.println("❌ Invalid account number!");
                return;
            }
            
            // Display transaction limits for the account type
            System.out.println("\n" + transactionService.getTransactionLimits(selectedAccount.getAccountType()));
            System.out.println();
            
            double amount = getDoubleInput(scanner, "Enter amount to withdraw: ");
            
            // Password authentication for withdrawal
            System.out.print("🔒 Enter your password to confirm withdrawal: ");
            String password = scanner.nextLine();
            
            // Verify password
            Customer verifiedCustomer = customerService.loginCustomer(customer.getEmail(), password);
            if (verifiedCustomer == null) {
                System.out.println("❌ Incorrect password! Withdrawal denied.");
                return;
            }
            
            try {
                Transaction transaction = transactionService.withdrawMoneyWithTransaction(accountNumber, amount, password);
                if (transaction != null && "SUCCESS".equals(transaction.getStatus())) {
                    Account updatedAccount = accountService.getAccountByNumber(accountNumber);
                    System.out.println("✓ Withdraw Successful");
                    System.out.println("Transaction ID: " + transaction.getTransactionId());
                    System.out.println("Amount Withdrawn: INR " + amount);
                    System.out.println("Remaining Balance: INR " + updatedAccount.getBalance());
                } else {
                    System.out.println("❌ Withdrawal failed! Please try again.");
                    System.out.println("Current Balance: INR " + selectedAccount.getBalance());
                }
            } catch (IllegalArgumentException e) {
                // Display error message
                System.out.println("\n❌ Withdrawal failed! " + e.getMessage());
                System.out.println("Current Balance: INR " + selectedAccount.getBalance());
            }
        } catch (Exception e) {
            System.out.println("❌ Withdrawal failed: " + e.getMessage());
        }
    }
    
    private void transferMoney(Scanner scanner, Customer customer) {
        System.out.println("\n=== Transfer Money ===");
        
        // Get customer's accounts
        List<Account> accounts = accountService.getAccountsByCustomerId(customer.getId());
        
        if (accounts.isEmpty()) {
            System.out.println("No accounts found. Please create an account first.");
            return;
        }
        
        // Display accounts
        System.out.println("\nYour Accounts:");
        for (int i = 0; i < accounts.size(); i++) {
            Account acc = accounts.get(i);
            System.out.println((i + 1) + ". Ac No: " + acc.getAccountNumber() + 
                             " - Type: " + acc.getAccountType() + 
                             " - Balance: INR " + acc.getBalance());
        }
        
        System.out.print("\nSelect from account number (1-" + accounts.size() + "): ");
        int fromChoice = scanner.nextInt();
        scanner.nextLine();
        
        if (fromChoice < 1 || fromChoice > accounts.size()) {
            System.out.println("Invalid choice!");
            return;
        }
        
        Account selectedAccount = accounts.get(fromChoice - 1);
        String fromAccountNumber = selectedAccount.getAccountNumber();
        
        // Display transaction limits for selected account
        String limitsInfo = transactionService.getTransactionLimits(selectedAccount.getAccountType());
        System.out.println(limitsInfo);
        
        System.out.print("Enter to account number: ");
        String toAccountNumber = scanner.nextLine();
        
        System.out.print("Enter amount to transfer: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        
        // Password authentication for transfer
        System.out.print("🔒 Enter your password to confirm transfer: ");
        String password = scanner.nextLine();
        
        // Verify password
        Customer verifiedCustomer = customerService.loginCustomer(customer.getEmail(), password);
        if (verifiedCustomer == null) {
            System.out.println("❌ Incorrect password! Transfer denied.");
            return;
        }
        
        try {
            Transaction transaction = transactionService.transferMoney(fromAccountNumber, toAccountNumber, amount, password);
            if (transaction != null && "SUCCESS".equals(transaction.getStatus())) {
                System.out.println("\n✓ Transfer Successful!");
                System.out.println("Transaction ID: " + transaction.getTransactionId());
                System.out.println("From Account: " + fromAccountNumber);
                System.out.println("To Account: " + toAccountNumber);
                System.out.println("Amount Transferred: INR " + amount);
                
                // Get updated balance
                Account updatedAccount = accountService.getAccountByNumber(fromAccountNumber);
                System.out.println("Remaining Balance: INR " + updatedAccount.getBalance());
            }
        } catch (IllegalArgumentException e) {
            // Display error message
            System.out.println("\n❌ Transfer failed! " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n❌ Transfer failed! Please try again.");
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void closeAccount(Scanner scanner, Customer customer) {
        try {
            List<Account> accounts = accountService.getAccountsByCustomerId(customer.getId());
            if (accounts.isEmpty()) {
                System.out.println("No accounts found!");
                return;
            }
            
            System.out.println("\n=== Close Account ===");
            for (Account acc : accounts) {
                System.out.println("Ac No: " + acc.getAccountNumber() + 
                                 " - Type: " + acc.getAccountType() +
                                 " - Balance: INR " + acc.getBalance());
            }
            
            System.out.print("Enter account number to close: ");
            String accountNumber = scanner.nextLine();
            
            // Get the account to show balance
            Account selectedAccount = accounts.stream()
                .filter(acc -> acc.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElse(null);
            
            if (selectedAccount == null) {
                System.out.println("❌ Account not found!");
                return;
            }
            
            // Show warning if account has balance
            if (selectedAccount.getBalance() > 0) {
                System.out.println("\n⚠️  Warning: This account has a balance of INR " + 
                                 String.format("%.2f", selectedAccount.getBalance()));
                System.out.println("💰 All funds will be automatically withdrawn before closing the account.");
                System.out.print("\nDo you want to proceed? (yes/no): ");
                String confirm = scanner.nextLine();
                
                if (!confirm.equalsIgnoreCase("yes") && !confirm.equalsIgnoreCase("y")) {
                    System.out.println("Account closure cancelled.");
                    return;
                }
            }
            
            // Ask for password confirmation
            System.out.print("\n🔒 Enter your password to confirm account closure: ");
            String password = scanner.nextLine();
            
            Map<String, Object> result = accountService.closeAccount(accountNumber, customer.getId(), password);
            
            if ((Boolean) result.get("success")) {
                System.out.println("\n✓ Account Closed Successfully!");
                
                double withdrawnAmount = (Double) result.get("withdrawnAmount");
                if (withdrawnAmount > 0) {
                    System.out.println("💰 Withdrawn Amount: INR " + String.format("%.2f", withdrawnAmount));
                    System.out.println("📄 Transaction ID: " + result.get("transactionId"));
                }
                
                System.out.println("🏦 Account Number: " + result.get("accountNumber"));
                System.out.println("📋 Status: Closed");
            } else {
                System.out.println("❌ Failed to close account.");
            }
        } catch (Exception e) {
            System.out.println("❌ Account closure failed: " + e.getMessage());
        }
    }
    
    private void changePassword(Scanner scanner, Customer customer) {
        System.out.println("\n=== Change Password ===");
        System.out.print("Enter current password: ");
        String oldPassword = scanner.nextLine();
        
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();
        
        System.out.print("Confirm new password: ");
        String confirmPassword = scanner.nextLine();
        
        if (!newPassword.equals(confirmPassword)) {
            System.out.println("Passwords do not match!");
            return;
        }
        
        try {
            boolean success = customerService.changePassword(customer.getId(), oldPassword, newPassword);
            if (success) {
                System.out.println("Password changed successfully!");
            } else {
                System.out.println("Password change failed! Please check your current password.");
            }
        } catch (Exception e) {
            System.out.println("Password change failed: " + e.getMessage());
        }
    }
    
    private void depositMoney(Scanner scanner, Customer customer) {
        try {
            List<Account> accounts = accountService.getAccountsByCustomerId(customer.getId());
            if (accounts.isEmpty()) {
                System.out.println("No accounts found!");
                return;
            }
            
            System.out.println("\n=== Deposit Money ===");
            for (Account acc : accounts) {
                System.out.println("Ac No: " + acc.getAccountNumber() + 
                                 " - Type: " + acc.getAccountType() +
                                 " - Balance: INR " + acc.getBalance());
            }
            
            System.out.print("Enter account number to deposit: ");
            String accountNumber = scanner.nextLine();
            
            Account selectedAccount = accounts.stream()
                .filter(acc -> acc.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElse(null);
                
            if (selectedAccount == null) {
                System.out.println("❌ Invalid account number!");
                return;
            }
            
            // Display transaction limits for the account type
            System.out.println("\n" + transactionService.getTransactionLimits(selectedAccount.getAccountType()));
            System.out.println();
            
            double amount = getDoubleInput(scanner, "Enter amount to deposit: ");
            
            if (amount <= 0) {
                System.out.println("❌ Deposit amount must be positive!");
                return;
            }
            
            try {
                Transaction transaction = transactionService.depositMoney(accountNumber, amount);
                if (transaction != null && "SUCCESS".equals(transaction.getStatus())) {
                    Account updatedAccount = accountService.getAccountByNumber(accountNumber);
                    System.out.println("✓ Deposit Successful!");
                    System.out.println("Amount Deposited: INR " + amount);
                    System.out.println("New Balance: INR " + updatedAccount.getBalance());
                } else {
                    System.out.println("❌ Deposit failed! Please try again.");
                    System.out.println("Current Balance: INR " + selectedAccount.getBalance());
                }
            } catch (IllegalArgumentException e) {
                // Display error message
                System.out.println("\n❌ Deposit failed! " + e.getMessage());
                System.out.println("Current Balance: INR " + selectedAccount.getBalance());
            }
        } catch (Exception e) {
            System.out.println("❌ Deposit failed: " + e.getMessage());
        }
    }
    
    // Undo last transaction
    private void undoLastTransaction(Scanner scanner, Customer customer) {
        System.out.println("\n=== Undo Last Transaction ===");
        
        try {
            // Check if undo is available
            if (!transactionService.canUndo()) {
                System.out.println("No transaction available to undo!");
                return;
            }
            
            // Get customer accounts
            List<Account> accounts = accountService.getAccountsByCustomerId(customer.getId());
            if (accounts.isEmpty()) {
                System.out.println("No accounts found!");
                return;
            }
            
            // Display accounts
            System.out.println("\nYour Accounts:");
            System.out.println("0. All Accounts (Undo last transaction across all accounts)");
            for (int i = 0; i < accounts.size(); i++) {
                Account acc = accounts.get(i);
                System.out.println((i + 1) + ". " + acc.getAccountNumber() + " - " + 
                                 (acc.getAccountType().equals("SB") ? "Savings Bank" : "Current Account") +
                                 " (Balance: INR " + acc.getBalance() + ")");
            }
            
            int choice = getIntInput(scanner, "\nSelect account (0 for all accounts): ");
            
            String accountNumber = null;
            if (choice > 0 && choice <= accounts.size()) {
                accountNumber = accounts.get(choice - 1).getAccountNumber();
            } else if (choice != 0) {
                System.out.println("❌ Invalid selection! Please choose 0 or a valid account number from the list.");
                return;
            }
            
            String confirmMessage = accountNumber != null 
                ? "Are you sure you want to undo the last transaction for account " + accountNumber + "? (yes/no): "
                : "Are you sure you want to undo the last transaction across all accounts? (yes/no): ";
            
            System.out.print(confirmMessage);
            String confirmation = scanner.nextLine();
            
            if (!confirmation.equalsIgnoreCase("yes")) {
                System.out.println("Undo cancelled.");
                return;
            }
            
            boolean success;
            if (accountNumber != null) {
                success = transactionService.undoLastTransactionForAccount(accountNumber);
            } else {
                success = transactionService.undoLastTransaction();
            }
            
            if (success) {
                System.out.println("✓ Transaction UNDONE successfully!");
                System.out.println("The last transaction has been reversed.");
            } else {
                System.out.println("✗ Undo failed!");
            }
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Undo failed: " + e.getMessage());
        }
    }
    
    // Redo transaction
    private void redoTransaction(Scanner scanner, Customer customer) {
        System.out.println("\n=== Redo Transaction ===");
        
        try {
            // Check if redo is available
            if (!transactionService.canRedo()) {
                System.out.println("No transaction available to redo!");
                return;
            }
            
            // Get customer accounts
            List<Account> accounts = accountService.getAccountsByCustomerId(customer.getId());
            if (accounts.isEmpty()) {
                System.out.println("No accounts found!");
                return;
            }
            
            // Display accounts
            System.out.println("\nYour Accounts:");
            System.out.println("0. All Accounts (Redo last transaction across all accounts)");
            for (int i = 0; i < accounts.size(); i++) {
                Account acc = accounts.get(i);
                System.out.println((i + 1) + ". " + acc.getAccountNumber() + " - " + 
                                 (acc.getAccountType().equals("SB") ? "Savings Bank" : "Current Account") +
                                 " (Balance: INR " + acc.getBalance() + ")");
            }
            
            int choice = getIntInput(scanner, "\nSelect account (0 for all accounts): ");
            
            String accountNumber = null;
            if (choice > 0 && choice <= accounts.size()) {
                accountNumber = accounts.get(choice - 1).getAccountNumber();
            } else if (choice != 0) {
                System.out.println("❌ Invalid selection! Please choose 0 or a valid account number from the list.");
                return;
            }
            
            String confirmMessage = accountNumber != null 
                ? "Are you sure you want to redo the last undone transaction for account " + accountNumber + "? (yes/no): "
                : "Are you sure you want to redo the last undone transaction across all accounts? (yes/no): ";
            
            System.out.print(confirmMessage);
            String confirmation = scanner.nextLine();
            
            if (!confirmation.equalsIgnoreCase("yes")) {
                System.out.println("Redo cancelled.");
                return;
            }
            
            boolean success;
            if (accountNumber != null) {
                success = transactionService.redoLastTransactionForAccount(accountNumber);
            } else {
                success = transactionService.redoLastTransaction();
            }
            
            if (success) {
                System.out.println("✓ Transaction REDONE successfully!");
                System.out.println("The transaction has been re-executed.");
            } else {
                System.out.println("✗ Redo failed!");
            }
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Redo failed: " + e.getMessage());
        }
    }
    
    // View undo/redo history
    private void viewUndoRedoHistory(Scanner scanner, Customer customer) {
        System.out.println("\n=== Undo/Redo History ===");
        
        try {
            // Get history summary
            TransactionService.TransactionHistorySummary summary = transactionService.getTransactionHistorySummary();
            
            System.out.println("Transaction History Summary:");
            System.out.println("---------------------------");
            System.out.println("Undoable Transactions: " + summary.getUndoStackSize());
            System.out.println("Redoable Transactions: " + summary.getRedoStackSize());
            System.out.println("Can Undo: " + (summary.isCanUndo() ? "✓ Yes" : "✗ No"));
            System.out.println("Can Redo: " + (summary.isCanRedo() ? "✓ Yes" : "✗ No"));
            System.out.println();
            
            // Show undoable transactions
            List<Transaction> undoableTransactions = transactionService.getUndoableTransactions();
            if (!undoableTransactions.isEmpty()) {
                System.out.println("=== Undoable Transactions ===");
                System.out.printf("%-20s %-12s %-10s %-15s %-20s%n", 
                                "Transaction ID", "Type", "Amount", "Status", "Date");
                System.out.println("=".repeat(85));
                
                for (int i = undoableTransactions.size() - 1; i >= 0 && i >= undoableTransactions.size() - 5; i--) {
                    Transaction txn = undoableTransactions.get(i);
                    System.out.printf("%-20s %-12s %-10.2f %-15s %-20s%n",
                                    txn.getTransactionId(),
                                    txn.getTransactionType(),
                                    txn.getAmount(),
                                    txn.getStatus(),
                                    txn.getTransactionDate());
                }
                System.out.println("(Showing last 5 transactions)");
            } else {
                System.out.println("No undoable transactions.");
            }
            
            System.out.println();
            
            // Show redoable transactions
            List<Transaction> redoableTransactions = transactionService.getRedoableTransactions();
            if (!redoableTransactions.isEmpty()) {
                System.out.println("=== Redoable Transactions ===");
                System.out.printf("%-20s %-12s %-10s %-15s %-20s%n", 
                                "Transaction ID", "Type", "Amount", "Status", "Date");
                System.out.println("=".repeat(85));
                
                for (int i = redoableTransactions.size() - 1; i >= 0 && i >= redoableTransactions.size() - 5; i--) {
                    Transaction txn = redoableTransactions.get(i);
                    System.out.printf("%-20s %-12s %-10.2f %-15s %-20s%n",
                                    txn.getTransactionId(),
                                    txn.getTransactionType(),
                                    txn.getAmount(),
                                    txn.getStatus(),
                                    txn.getTransactionDate());
                }
                System.out.println("(Showing last 5 transactions)");
            } else {
                System.out.println("No redoable transactions.");
            }
            
        } catch (Exception e) {
            System.out.println("Error viewing undo/redo history: " + e.getMessage());
        }
    }
    
    // View audit logs
    private void viewAuditLogs(Scanner scanner, Customer customer) {
        System.out.println("\n=== Audit Logs ===");
        System.out.println("1. All Logs");
        System.out.println("2. Transaction Logs");
        System.out.println("3. Security Logs");
        System.out.println("4. Recent Logs (Last 24 hours)");
        
        int choice = getIntInput(scanner, "Enter your choice: ");
        
        try {
            List<AuditLog> logs = null;
            String title = "";
            
            switch (choice) {
                case 1:
                    logs = auditLogService.getAuditLogsByUserId(customer.getId());
                    title = "All Audit Logs";
                    break;
                case 2:
                    logs = auditLogService.getAuditLogsByUserId(customer.getId());
                    logs = logs.stream()
                             .filter(log -> "TRANSACTION".equals(log.getEntityType()))
                             .collect(java.util.stream.Collectors.toList());
                    title = "Transaction Audit Logs";
                    break;
                case 3:
                    logs = auditLogService.getAuditLogsByUserId(customer.getId());
                    logs = logs.stream()
                             .filter(log -> {
                                 String action = log.getAction();
                                 return "LOGIN".equals(action) || "LOGOUT".equals(action) || 
                                        "FAILED_LOGIN".equals(action) || "PASSWORD_CHANGE".equals(action);
                             })
                             .collect(java.util.stream.Collectors.toList());
                    title = "Security Audit Logs";
                    break;
                case 4:
                    logs = auditLogService.getRecentAuditLogs(24);
                    logs = logs.stream()
                             .filter(log -> customer.getId().equals(log.getUserId()))
                             .collect(java.util.stream.Collectors.toList());
                    title = "Recent Audit Logs (Last 24 Hours)";
                    break;
                default:
                    System.out.println("❌ Invalid choice! Please select a valid option (1-4).");
                    return;
            }
            
            if (logs == null || logs.isEmpty()) {
                System.out.println("No audit logs found.");
                return;
            }
            
            System.out.println("\n=== " + title + " ===");
            System.out.printf("%-20s %-20s %-15s %-15s %-30s%n", 
                            "Timestamp", "Action", "Entity Type", "Entity ID", "Description");
            System.out.println("=".repeat(105));
            
            int count = 0;
            for (AuditLog log : logs) {
                if (count >= 20) { // Show only last 20 logs
                    break;
                }
                
                String description = log.getNewValue() != null ? log.getNewValue() : "-";
                if (description.length() > 28) {
                    description = description.substring(0, 25) + "...";
                }
                
                System.out.printf("%-20s %-20s %-15s %-15s %-30s%n",
                                log.getTimestamp(),
                                formatAction(log.getAction()),
                                log.getEntityType(),
                                log.getEntityId() != null ? log.getEntityId() : "-",
                                description);
                count++;
            }
            
            System.out.println("\n(Showing last " + count + " logs)");
            
            // Show summary
            System.out.println("\n=== Audit Summary ===");
            System.out.println("Total Logs: " + logs.size());
            long transactionCount = logs.stream()
                                       .filter(log -> "TRANSACTION".equals(log.getEntityType()))
                                       .count();
            long securityCount = logs.stream()
                                    .filter(log -> {
                                        String action = log.getAction();
                                        return "LOGIN".equals(action) || "FAILED_LOGIN".equals(action) || 
                                               "PASSWORD_CHANGE".equals(action);
                                    })
                                    .count();
            System.out.println("Transaction Logs: " + transactionCount);
            System.out.println("Security Logs: " + securityCount);
            
        } catch (Exception e) {
            System.out.println("Error viewing audit logs: " + e.getMessage());
        }
    }
    
    // Helper method to format action names
    private String formatAction(String action) {
        switch (action) {
            case "LOGIN": return "🔐 Login";
            case "LOGOUT": return "🚪 Logout";
            case "FAILED_LOGIN": return "❌ Failed Login";
            case "DEPOSIT": return "💵 Deposit";
            case "WITHDRAW": return "💸 Withdraw";
            case "TRANSFER_OUT": return "➡️ Transfer Out";
            case "TRANSFER_IN": return "⬅️ Transfer In";
            case "TRANSACTION_UNDO": return "↶ Undo";
            case "TRANSACTION_REDO": return "↷ Redo";
            case "TRANSACTION_UNDOABLE": return "📝 Transaction";
            case "ACCOUNT_CREATION": return "🏦 Account Created";
            case "ACCOUNT_CLOSURE": return "🔒 Account Closed";
            case "PASSWORD_CHANGE": return "🔑 Password Changed";
            case "BALANCE_UPDATE": return "💰 Balance Update";
            default: return action;
        }
    }
    
}