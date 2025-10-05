package com.yourcompany.bankingsystem.service;

import com.yourcompany.bankingsystem.model.Account;
import com.yourcompany.bankingsystem.model.Customer;
import com.yourcompany.bankingsystem.model.Transaction;
import com.yourcompany.bankingsystem.model.custom.TransactionQueue;
import com.yourcompany.bankingsystem.model.custom.TransactionStack;
import com.yourcompany.bankingsystem.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {
    
    @Mock
    private TransactionRepository transactionRepository;
    
    @Mock
    private AccountService accountService;
    
    @Mock
    private CustomerService customerService;
    
    @Mock
    private AuditLogService auditLogService;
    
    @Mock
    private TransactionQueue transactionQueue;
    
    @Mock
    private TransactionStack transactionStack;
    
    @InjectMocks
    private TransactionService transactionService;
    
    private Account testAccount;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testAccount = new Account("customer1", "SB", 10000.0);
        testAccount.setAccountNumber("12345");
    }
    
    @Test
    void testDepositMoney_Success() {
        // Given
        Transaction savedTransaction = new Transaction();
        savedTransaction.setId("txn123");
        savedTransaction.setTransactionId("TXN001");
        savedTransaction.setStatus("SUCCESS");
        
        when(accountService.getAccountByNumber("12345")).thenReturn(testAccount);
        when(transactionQueue.enqueue(any(Transaction.class))).thenReturn(true);
        when(accountService.updateBalance("12345", 12000.0)).thenReturn(testAccount);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);
        when(transactionStack.push(any(Transaction.class))).thenReturn(true);
        
        // When
        Transaction result = transactionService.depositMoney("12345", 2000.0);
        
        // Then
        assertNotNull(result);
        assertEquals("SUCCESS", result.getStatus());
        verify(accountService).updateBalance("12345", 12000.0);
        verify(transactionRepository).save(any(Transaction.class));
        verify(auditLogService).logAction(anyString(), eq("DEPOSIT"), eq("TRANSACTION"), anyString());
    }
    
    @Test
    void testDepositMoney_InvalidAmount() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> transactionService.depositMoney("12345", -1000.0)
        );
        assertEquals("Deposit amount must be positive", exception.getMessage());
    }
    
    @Test
    void testWithdrawMoney_Success() {
        // Given
        Transaction savedTransaction = new Transaction();
        savedTransaction.setId("txn124");
        savedTransaction.setTransactionId("TXN002");
        
        when(accountService.validateAccountForTransaction("12345", 1000.0, "WITHDRAW")).thenReturn(true);
        when(accountService.getAccountByNumber("12345")).thenReturn(testAccount);
        when(transactionQueue.enqueue(any(Transaction.class))).thenReturn(true);
        when(accountService.updateBalance("12345", 9000.0)).thenReturn(testAccount);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);
        when(transactionStack.push(any(Transaction.class))).thenReturn(true);
        
        // When
        boolean result = transactionService.withdrawMoney("12345", 1000.0);
        
        // Then
        assertTrue(result);
        verify(accountService).updateBalance("12345", 9000.0);
        verify(transactionRepository).save(any(Transaction.class));
        verify(auditLogService).logAction(anyString(), eq("WITHDRAW"), eq("TRANSACTION"), anyString());
    }
    
    @Test
    void testWithdrawMoney_InsufficientBalance() {
        // Given
        when(accountService.validateAccountForTransaction("12345", 15000.0, "WITHDRAW")).thenReturn(false);
        
        // When
        boolean result = transactionService.withdrawMoney("12345", 15000.0);
        
        // Then
        assertFalse(result);
        verify(accountService, never()).updateBalance(anyString(), anyDouble());
    }
    
    @Test
    void testTransferMoney_Success() {
        // Given
        Account toAccount = new Account("customer2", "CA", 5000.0);
        toAccount.setAccountNumber("67890");
        
        Customer customer = new Customer();
        customer.setId("customer1");
        customer.setEmail("test@example.com");
        
        Transaction savedTransaction = new Transaction();
        savedTransaction.setId("txn125");
        savedTransaction.setTransactionId("TXN003");
        
        when(accountService.getAccountByNumber("12345")).thenReturn(testAccount);
        when(accountService.getAccountByNumber("67890")).thenReturn(toAccount);
        when(accountService.validateAccountForTransaction("12345", 2000.0, "TRANSFER")).thenReturn(true);
        when(transactionQueue.enqueue(any(Transaction.class))).thenReturn(true);
        when(accountService.updateBalance("12345", 8000.0)).thenReturn(testAccount);
        when(accountService.updateBalance("67890", 7000.0)).thenReturn(toAccount);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);
        when(transactionStack.push(any(Transaction.class))).thenReturn(true);
        
        // Mock customer authentication
        when(customerService.verifyCustomerPassword("customer1", "password123")).thenReturn(true);
        
        // When
        Transaction result = transactionService.transferMoney("12345", "67890", 2000.0, "password123");
        
        // Then
        assertNotNull(result);
        verify(accountService).updateBalance("12345", 8000.0);
        verify(accountService).updateBalance("67890", 7000.0);
        verify(transactionRepository).save(any(Transaction.class));
        verify(auditLogService, times(2)).logAction(anyString(), anyString(), eq("TRANSACTION"), anyString());
    }
    
    @Test
    void testTransferMoney_SameAccount() {
        // Given
        when(accountService.getAccountByNumber("12345")).thenReturn(testAccount);
        when(customerService.verifyCustomerPassword("customer1", "password123")).thenReturn(true);
        
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> transactionService.transferMoney("12345", "12345", 1000.0, "password123")
        );
        assertEquals("Cannot transfer to same account", exception.getMessage());
    }
    
    @Test
    void testTransferMoney_InvalidAmount() {
        // Given
        when(accountService.getAccountByNumber("12345")).thenReturn(testAccount);
        when(customerService.verifyCustomerPassword("customer1", "password123")).thenReturn(true);
        
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> transactionService.transferMoney("12345", "67890", -1000.0, "password123")
        );
        assertEquals("Transfer amount must be positive", exception.getMessage());
    }
}
