package com.yourcompany.bankingsystem.model.custom;

import com.yourcompany.bankingsystem.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionStack {
    
    private List<Transaction> stack;
    private int maxSize;
    
    public TransactionStack() {
        this.stack = new ArrayList<>();
        this.maxSize = 1000; // Default max size
    }
    
    public TransactionStack(int maxSize) {
        this.stack = new ArrayList<>();
        this.maxSize = maxSize;
    }
    
    // Push operation - add transaction to top of stack
    public boolean push(Transaction transaction) {
        if (stack.size() >= maxSize) {
            return false; // Stack overflow
        }
        stack.add(transaction);
        return true;
    }
    
    // Pop operation - remove and return top transaction
    public Transaction pop() {
        if (isEmpty()) {
            return null; // Stack underflow
        }
        return stack.remove(stack.size() - 1);
    }
    
    // Peek operation - return top transaction without removing
    public Transaction peek() {
        if (isEmpty()) {
            return null;
        }
        return stack.get(stack.size() - 1);
    }
    
    // Check if stack is empty
    public boolean isEmpty() {
        return stack.isEmpty();
    }
    
    // Get current size of stack
    public int size() {
        return stack.size();
    }
    
    // Check if stack is full
    public boolean isFull() {
        return stack.size() >= maxSize;
    }
    
    // Clear all transactions from stack
    public void clear() {
        stack.clear();
    }
    
    // Get all transactions in LIFO order
    public List<Transaction> getAllTransactions() {
        List<Transaction> result = new ArrayList<>();
        for (int i = stack.size() - 1; i >= 0; i--) {
            result.add(stack.get(i));
        }
        return result;
    }
    
    // Search for a transaction in the stack
    public int search(String transactionId) {
        for (int i = stack.size() - 1; i >= 0; i--) {
            if (stack.get(i).getTransactionId().equals(transactionId)) {
                return stack.size() - i; // Return position from top (1-based)
            }
        }
        return -1; // Not found
    }
    
    @Override
    public String toString() {
        return "TransactionStack{" +
                "size=" + stack.size() +
                ", maxSize=" + maxSize +
                ", isEmpty=" + isEmpty() +
                ", isFull=" + isFull() +
                '}';
    }
}
