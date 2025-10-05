package com.yourcompany.bankingsystem.model.custom;

import com.yourcompany.bankingsystem.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionQueue {
    
    private List<Transaction> queue;
    private int maxSize;
    private int front;
    private int rear;
    
    public TransactionQueue() {
        this.queue = new ArrayList<>();
        this.maxSize = 1000; // Default max size
        this.front = 0;
        this.rear = -1;
    }
    
    public TransactionQueue(int maxSize) {
        this.queue = new ArrayList<>();
        this.maxSize = maxSize;
        this.front = 0;
        this.rear = -1;
    }
    
    // Enqueue operation - add transaction to rear of queue
    public boolean enqueue(Transaction transaction) {
        if (isFull()) {
            return false; // Queue overflow
        }
        queue.add(transaction);
        rear++;
        return true;
    }
    
    // Dequeue operation - remove and return front transaction
    public Transaction dequeue() {
        if (isEmpty()) {
            return null; // Queue underflow
        }
        Transaction transaction = queue.get(front);
        front++;
        
        // Reset queue when it becomes empty for efficiency
        if (front > rear) {
            front = 0;
            rear = -1;
            queue.clear();
        }
        
        return transaction;
    }
    
    // Peek operation - return front transaction without removing
    public Transaction peek() {
        if (isEmpty()) {
            return null;
        }
        return queue.get(front);
    }
    
    // Check if queue is empty
    public boolean isEmpty() {
        return front > rear || queue.isEmpty();
    }
    
    // Get current size of queue
    public int size() {
        if (isEmpty()) {
            return 0;
        }
        return rear - front + 1;
    }
    
    // Check if queue is full
    public boolean isFull() {
        return size() >= maxSize;
    }
    
    // Clear all transactions from queue
    public void clear() {
        queue.clear();
        front = 0;
        rear = -1;
    }
    
    // Get all transactions in FIFO order
    public List<Transaction> getAllTransactions() {
        List<Transaction> result = new ArrayList<>();
        for (int i = front; i <= rear && i < queue.size(); i++) {
            result.add(queue.get(i));
        }
        return result;
    }
    
    // Search for a transaction in the queue
    public int search(String transactionId) {
        for (int i = front; i <= rear && i < queue.size(); i++) {
            if (queue.get(i).getTransactionId().equals(transactionId)) {
                return i - front + 1; // Return position from front (1-based)
            }
        }
        return -1; // Not found
    }
    
    // Get transaction at specific position (for monitoring)
    public Transaction getAt(int position) {
        if (position < 1 || position > size()) {
            return null;
        }
        return queue.get(front + position - 1);
    }
    
    @Override
    public String toString() {
        return "TransactionQueue{" +
                "size=" + size() +
                ", maxSize=" + maxSize +
                ", front=" + front +
                ", rear=" + rear +
                ", isEmpty=" + isEmpty() +
                ", isFull=" + isFull() +
                '}';
    }
}
