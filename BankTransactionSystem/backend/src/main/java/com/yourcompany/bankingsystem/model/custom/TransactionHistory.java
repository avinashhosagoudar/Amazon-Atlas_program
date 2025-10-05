package com.yourcompany.bankingsystem.model.custom;

import com.yourcompany.bankingsystem.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionHistory {
    
    private List<Transaction> undoStack;
    private List<Transaction> redoStack;
    private int maxSize;
    
    public TransactionHistory() {
        this.undoStack = new ArrayList<>();
        this.redoStack = new ArrayList<>();
        this.maxSize = 100; // Keep last 100 undoable transactions
    }
    
    // Push a transaction to undo stack (clear redo stack as new action performed)
    public void pushToUndoStack(Transaction transaction) {
        if (undoStack.size() >= maxSize) {
            undoStack.remove(0); // Remove oldest if at capacity
        }
        undoStack.add(transaction);
        redoStack.clear(); // Clear redo stack when new transaction is performed
    }
    
    // Pop from undo stack and push to redo stack
    public Transaction popFromUndoStack() {
        if (undoStack.isEmpty()) {
            return null;
        }
        Transaction transaction = undoStack.remove(undoStack.size() - 1);
        redoStack.add(transaction);
        return transaction;
    }
    
    // Pop from redo stack and push back to undo stack
    public Transaction popFromRedoStack() {
        if (redoStack.isEmpty()) {
            return null;
        }
        Transaction transaction = redoStack.remove(redoStack.size() - 1);
        undoStack.add(transaction);
        return transaction;
    }
    
    // Peek at last undoable transaction
    public Transaction peekUndoStack() {
        if (undoStack.isEmpty()) {
            return null;
        }
        return undoStack.get(undoStack.size() - 1);
    }
    
    // Peek at last redoable transaction
    public Transaction peekRedoStack() {
        if (redoStack.isEmpty()) {
            return null;
        }
        return redoStack.get(redoStack.size() - 1);
    }
    
    // Check if undo is available
    public boolean canUndo() {
        return !undoStack.isEmpty();
    }
    
    // Check if redo is available
    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
    
    // Get undo stack size
    public int getUndoStackSize() {
        return undoStack.size();
    }
    
    // Get redo stack size
    public int getRedoStackSize() {
        return redoStack.size();
    }
    
    // Get all undoable transactions (for display)
    public List<Transaction> getUndoableTransactions() {
        return new ArrayList<>(undoStack);
    }
    
    // Get all redoable transactions (for display)
    public List<Transaction> getRedoableTransactions() {
        return new ArrayList<>(redoStack);
    }
    
    // Clear both stacks
    public void clear() {
        undoStack.clear();
        redoStack.clear();
    }
    
    // Clear redo stack only
    public void clearRedoStack() {
        redoStack.clear();
    }
    
    @Override
    public String toString() {
        return "TransactionHistory{" +
                "undoStackSize=" + undoStack.size() +
                ", redoStackSize=" + redoStack.size() +
                ", canUndo=" + canUndo() +
                ", canRedo=" + canRedo() +
                '}';
    }
}
