package com.yourcompany.bankingsystem.service;

import com.yourcompany.bankingsystem.model.Account;
import com.yourcompany.bankingsystem.model.Transaction;
import com.yourcompany.bankingsystem.model.custom.TransactionHistory;
import com.yourcompany.bankingsystem.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TransactionHistoryTest {
    
    @Mock
    private TransactionRepository transactionRepository;
    
    @Mock
    private AccountService accountService;
    
    @Mock
    private AuditLogService auditLogService;
    
    @InjectMocks
    private TransactionService transactionService;
    
    private TransactionHistory transactionHistory;
    private Account testAccount;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionHistory = new TransactionHistory();
        testAccount = new Account("customer1", "SB", 10000.0);
        testAccount.setAccountNumber("12345");
    }
    
    @Test
    void testPushToUndoStack() {
        Transaction transaction = new Transaction(null, "12345", 1000.0, "DEPOSIT", "Test deposit");
        transactionHistory.pushToUndoStack(transaction);
        
        assertTrue(transactionHistory.canUndo());
        assertEquals(1, transactionHistory.getUndoStackSize());
    }
    
    @Test
    void testPopFromUndoStack() {
        Transaction transaction = new Transaction(null, "12345", 1000.0, "DEPOSIT", "Test deposit");
        transactionHistory.pushToUndoStack(transaction);
        
        Transaction poppedTransaction = transactionHistory.popFromUndoStack();
        
        assertNotNull(poppedTransaction);
        assertFalse(transactionHistory.canUndo());
        assertTrue(transactionHistory.canRedo());
    }
    
    @Test
    void testUndoRedoCycle() {
        Transaction transaction = new Transaction(null, "12345", 1000.0, "DEPOSIT", "Test deposit");
        
        // Push to undo stack
        transactionHistory.pushToUndoStack(transaction);
        assertTrue(transactionHistory.canUndo());
        assertFalse(transactionHistory.canRedo());
        
        // Pop from undo (moves to redo)
        transactionHistory.popFromUndoStack();
        assertFalse(transactionHistory.canUndo());
        assertTrue(transactionHistory.canRedo());
        
        // Pop from redo (moves back to undo)
        transactionHistory.popFromRedoStack();
        assertTrue(transactionHistory.canUndo());
        assertFalse(transactionHistory.canRedo());
    }
    
    @Test
    void testClearRedoStackOnNewTransaction() {
        Transaction transaction1 = new Transaction(null, "12345", 1000.0, "DEPOSIT", "Test deposit 1");
        Transaction transaction2 = new Transaction(null, "12345", 2000.0, "DEPOSIT", "Test deposit 2");
        
        // Push and pop to create redo stack
        transactionHistory.pushToUndoStack(transaction1);
        transactionHistory.popFromUndoStack();
        assertTrue(transactionHistory.canRedo());
        
        // Push new transaction should clear redo stack
        transactionHistory.pushToUndoStack(transaction2);
        assertFalse(transactionHistory.canRedo());
    }
    
    @Test
    void testMaxStackSize() {
        // Push 150 transactions (max is 100)
        for (int i = 0; i < 150; i++) {
            Transaction transaction = new Transaction(null, "12345", 1000.0, "DEPOSIT", "Test " + i);
            transactionHistory.pushToUndoStack(transaction);
        }
        
        // Should only keep last 100
        assertEquals(100, transactionHistory.getUndoStackSize());
    }
}
