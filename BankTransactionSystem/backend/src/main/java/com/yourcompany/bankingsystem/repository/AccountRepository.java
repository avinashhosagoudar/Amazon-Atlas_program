package com.yourcompany.bankingsystem.repository;

import com.yourcompany.bankingsystem.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {
    
    // Find account by account number
    Optional<Account> findByAccountNumber(String accountNumber);
    
    // Find all accounts for a customer
    List<Account> findByCustomerId(String customerId);
    
    // Find active accounts for a customer
    @Query("{'customerId': ?0, 'active': true}")
    List<Account> findActiveAccountsByCustomerId(String customerId);
    
    // Find accounts by type
    List<Account> findByAccountType(String accountType);
    
    // Find accounts by customer ID and account type
    List<Account> findByCustomerIdAndAccountType(String customerId, String accountType);
    
    // Find active accounts by customer ID and account type
    @Query("{'customerId': ?0, 'accountType': ?1, 'active': true}")
    List<Account> findActiveAccountsByCustomerIdAndAccountType(String customerId, String accountType);
    
    // Find accounts with balance greater than specified amount
    @Query("{'balance': {$gt: ?0}}")
    List<Account> findAccountsWithBalanceGreaterThan(double amount);
    
    // Find accounts with balance less than specified amount
    @Query("{'balance': {$lt: ?0}}")
    List<Account> findAccountsWithBalanceLessThan(double amount);
    
    // Check if account number exists
    boolean existsByAccountNumber(String accountNumber);
    
    // Count accounts by customer
    long countByCustomerId(String customerId);
    
    // Find all active accounts
    @Query("{'active': true}")
    List<Account> findAllActiveAccounts();
    
    // Get total balance for all accounts of a customer
    @Query("{'customerId': ?0}")
    List<Account> findAccountsForTotalBalance(String customerId);
}
