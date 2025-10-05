package com.yourcompany.bankingsystem.repository;

import com.yourcompany.bankingsystem.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
    
    // Find transaction by transaction ID
    Optional<Transaction> findByTransactionId(String transactionId);
    
    // Find transactions by from account
    List<Transaction> findByFromAccountNumber(String accountNumber, Sort sort);
    
    // Find transactions by to account
    List<Transaction> findByToAccountNumber(String accountNumber, Sort sort);
    
    // Find all transactions for an account (both from and to)
    @Query("{'$or': [{'fromAccountNumber': ?0}, {'toAccountNumber': ?0}]}")
    List<Transaction> findByAccountNumber(String accountNumber, Sort sort);
    
    // Find transactions by type
    List<Transaction> findByTransactionType(String transactionType, Sort sort);
    
    // Find transactions by status
    List<Transaction> findByStatus(String status, Sort sort);
    
    // Find transactions within date range
    @Query("{'transactionDate': {$gte: ?0, $lte: ?1}}")
    List<Transaction> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate, Sort sort);
    
    // Find transactions for an account within date range
    @Query("{'$or': [{'fromAccountNumber': ?0}, {'toAccountNumber': ?0}], 'transactionDate': {$gte: ?1, $lte: ?2}}")
    List<Transaction> findByAccountNumberAndDateRange(String accountNumber, LocalDateTime startDate, LocalDateTime endDate, Sort sort);
    
    // Find successful transactions
    @Query("{'status': 'SUCCESS'}")
    List<Transaction> findSuccessfulTransactions(Sort sort);
    
    // Find failed transactions
    @Query("{'status': 'FAILED'}")
    List<Transaction> findFailedTransactions(Sort sort);
    
    // Find pending transactions
    @Query("{'status': 'PENDING'}")
    List<Transaction> findPendingTransactions();
    
    // Count transactions by status
    long countByStatus(String status);
    
    // Find transactions by amount range
    @Query("{'amount': {$gte: ?0, $lte: ?1}}")
    List<Transaction> findByAmountBetween(double minAmount, double maxAmount, Sort sort);
    
    // Find recent transactions for an account (last N transactions)
    @Query("{'$or': [{'fromAccountNumber': ?0}, {'toAccountNumber': ?0}]}")
    List<Transaction> findRecentTransactionsByAccountNumber(String accountNumber, Sort sort);
    
    // Get transaction statistics for settlement
    @Query("{'status': 'SUCCESS', 'transactionDate': {$gte: ?0, $lte: ?1}}")
    List<Transaction> findSuccessfulTransactionsForSettlement(LocalDateTime startDate, LocalDateTime endDate);
    
    // Find successful transactions for an account today (for daily limit checking)
    @Query("{'$or': [{'fromAccountNumber': ?0}, {'toAccountNumber': ?0}], 'status': 'SUCCESS', 'transactionDate': {$gte: ?1, $lte: ?2}}")
    List<Transaction> findSuccessfulTransactionsByAccountNumberAndDateRange(String accountNumber, LocalDateTime startDate, LocalDateTime endDate);
    
    // Find successful debit transactions (WITHDRAW/TRANSFER) for an account today
    @Query("{'fromAccountNumber': ?0, 'status': 'SUCCESS', 'transactionType': {$in: ['WITHDRAW', 'TRANSFER']}, 'transactionDate': {$gte: ?1, $lte: ?2}}")
    List<Transaction> findSuccessfulDebitTransactionsByAccountNumberAndDateRange(String accountNumber, LocalDateTime startDate, LocalDateTime endDate);
}
