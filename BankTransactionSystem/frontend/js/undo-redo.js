if (!requireLogin()) {
    // Redirect handled
}

const customer = getFromLocalStorage('customer');
let selectedAccount = null;

// Load customer accounts
async function loadAccounts() {
    try {
        const accounts = await apiCall(`/accounts/customer/${customer.id}`);
        
        const accountsList = document.getElementById('accountsList');
        accountsList.innerHTML = '<h4>Choose Account for Undo/Redo:</h4>';
        
        if (accounts && accounts.length > 0) {
            accounts.forEach(account => {
                const accountDiv = document.createElement('div');
                accountDiv.className = 'account-item';
                accountDiv.style.cssText = 'padding: 15px; margin: 10px 0; border: 2px solid #ddd; border-radius: 8px; cursor: pointer; transition: all 0.3s;';
                accountDiv.innerHTML = `
                    <strong>Account:</strong> ${account.accountNumber}<br>
                    <strong>Type:</strong> ${account.accountType === 'SB' ? 'Savings Bank' : 'Current Account'}<br>
                    <strong>Balance:</strong> INR ${account.balance.toFixed(2)}
                `;
                accountDiv.onclick = () => selectAccount(account);
                accountDiv.onmouseover = () => accountDiv.style.borderColor = '#4facfe';
                accountDiv.onmouseout = () => {
                    if (!selectedAccount || selectedAccount.accountNumber !== account.accountNumber) {
                        accountDiv.style.borderColor = '#ddd';
                    }
                };
                accountsList.appendChild(accountDiv);
            });
            
            // Add "All Accounts" option
            const allAccountsDiv = document.createElement('div');
            allAccountsDiv.className = 'account-item';
            allAccountsDiv.style.cssText = 'padding: 15px; margin: 10px 0; border: 2px solid #ddd; border-radius: 8px; cursor: pointer; transition: all 0.3s; background-color: #f0f0f0;';
            allAccountsDiv.innerHTML = `
                <strong>🌐 All Accounts</strong><br>
                <em>Undo/Redo across all accounts</em>
            `;
            allAccountsDiv.onclick = () => selectAccount(null);
            allAccountsDiv.onmouseover = () => allAccountsDiv.style.borderColor = '#4facfe';
            allAccountsDiv.onmouseout = () => {
                if (selectedAccount !== null) {
                    allAccountsDiv.style.borderColor = '#ddd';
                }
            };
            accountsList.appendChild(allAccountsDiv);
        } else {
            accountsList.innerHTML += '<p>No accounts found!</p>';
        }
    } catch (error) {
        showMessage('message', 'Error loading accounts: ' + error.message, 'error');
    }
}

function selectAccount(account) {
    selectedAccount = account;
    
    // Update UI to show selected account
    const accountItems = document.querySelectorAll('.account-item');
    accountItems.forEach(item => {
        item.style.borderColor = '#ddd';
        item.style.backgroundColor = '';
    });
    
    if (account) {
        event.target.closest('.account-item').style.borderColor = '#4facfe';
        event.target.closest('.account-item').style.backgroundColor = '#e3f2fd';
        
        document.getElementById('selectedAccountInfo').style.display = 'block';
        document.getElementById('selectedAccountNumber').textContent = account.accountNumber;
        document.getElementById('selectedAccountType').textContent = 
            account.accountType === 'SB' ? 'Savings Bank' : 'Current Account';
    } else {
        event.target.closest('.account-item').style.borderColor = '#4facfe';
        event.target.closest('.account-item').style.backgroundColor = '#e3f2fd';
        
        document.getElementById('selectedAccountInfo').style.display = 'block';
        document.getElementById('selectedAccountNumber').textContent = 'All Accounts';
        document.getElementById('selectedAccountType').textContent = 'Global Undo/Redo';
    }
    
    // Reload transactions for selected account
    loadHistorySummary();
    loadUndoableTransactions();
    loadRedoableTransactions();
}

// Load transaction history summary
async function loadHistorySummary() {
    try {
        const summary = await apiCall('/transaction-history/summary');
        
        const summaryDiv = document.getElementById('historySummary');
        summaryDiv.innerHTML = `
            <h3>Transaction History Summary</h3>
            <p><strong>Undoable Transactions:</strong> ${summary.undoStackSize}</p>
            <p><strong>Redoable Transactions:</strong> ${summary.redoStackSize}</p>
            <p><strong>Can Undo:</strong> ${summary.canUndo ? '✓ Yes' : '✗ No'}</p>
            <p><strong>Can Redo:</strong> ${summary.canRedo ? '✓ Yes' : '✗ No'}</p>
        `;

        // Update button states
        document.getElementById('undoBtn').disabled = !summary.canUndo;
        document.getElementById('redoBtn').disabled = !summary.canRedo;

    } catch (error) {
        showMessage('message', 'Error loading summary: ' + error.message, 'error');
    }
}

// Load undoable transactions
async function loadUndoableTransactions() {
    try {
        let endpoint = '/transaction-history/undoable';
        if (selectedAccount) {
            endpoint = `/transaction-history/undoable/${selectedAccount.accountNumber}`;
        }
        
        const transactions = await apiCall(endpoint);
        
        const undoableList = document.getElementById('undoableList');
        
        if (transactions && transactions.length > 0) {
            const table = document.createElement('table');
            table.className = 'transaction-table';
            table.innerHTML = `
                <thead>
                    <tr>
                        <th>Transaction ID</th>
                        <th>Type</th>
                        <th>From Account</th>
                        <th>To Account</th>
                        <th>Amount</th>
                        <th>Status</th>
                        <th>Date</th>
                    </tr>
                </thead>
                <tbody>
                    ${transactions.reverse().map(txn => `
                        <tr>
                            <td>${txn.transactionId}</td>
                            <td>${txn.transactionType}</td>
                            <td>${txn.fromAccountNumber || '-'}</td>
                            <td>${txn.toAccountNumber || '-'}</td>
                            <td>INR ${txn.amount.toFixed(2)}</td>
                            <td class="status-${txn.status.toLowerCase()}">${txn.status}</td>
                            <td>${new Date(txn.transactionDate).toLocaleString()}</td>
                        </tr>
                    `).join('')}
                </tbody>
            `;
            undoableList.innerHTML = '';
            undoableList.appendChild(table);
        } else {
            const accountInfo = selectedAccount ? ` for account ${selectedAccount.accountNumber}` : '';
            undoableList.innerHTML = `<p>No undoable transactions${accountInfo}.</p>`;
        }
    } catch (error) {
        console.error('Error loading undoable transactions:', error);
    }
}

// Load redoable transactions
async function loadRedoableTransactions() {
    try {
        let endpoint = '/transaction-history/redoable';
        if (selectedAccount) {
            endpoint = `/transaction-history/redoable/${selectedAccount.accountNumber}`;
        }
        
        const transactions = await apiCall(endpoint);
        
        const redoableList = document.getElementById('redoableList');
        
        if (transactions && transactions.length > 0) {
            const table = document.createElement('table');
            table.className = 'transaction-table';
            table.innerHTML = `
                <thead>
                    <tr>
                        <th>Transaction ID</th>
                        <th>Type</th>
                        <th>From Account</th>
                        <th>To Account</th>
                        <th>Amount</th>
                        <th>Status</th>
                        <th>Date</th>
                    </tr>
                </thead>
                <tbody>
                    ${transactions.reverse().map(txn => `
                        <tr>
                            <td>${txn.transactionId}</td>
                            <td>${txn.transactionType}</td>
                            <td>${txn.fromAccountNumber || '-'}</td>
                            <td>${txn.toAccountNumber || '-'}</td>
                            <td>INR ${txn.amount.toFixed(2)}</td>
                            <td class="status-${txn.status.toLowerCase()}">${txn.status}</td>
                            <td>${new Date(txn.transactionDate).toLocaleString()}</td>
                        </tr>
                    `).join('')}
                </tbody>
            `;
            redoableList.innerHTML = '';
            redoableList.appendChild(table);
        } else {
            const accountInfo = selectedAccount ? ` for account ${selectedAccount.accountNumber}` : '';
            redoableList.innerHTML = `<p>No redoable transactions${accountInfo}.</p>`;
        }
    } catch (error) {
        console.error('Error loading redoable transactions:', error);
    }
}

// Handle undo
async function handleUndo() {
    const accountInfo = selectedAccount 
        ? `for account ${selectedAccount.accountNumber} (${selectedAccount.accountType})` 
        : 'across all accounts';
    
    if (!confirm(`Are you sure you want to undo the last transaction ${accountInfo}?`)) {
        return;
    }

    try {
        let endpoint = '/transaction-history/undo';
        if (selectedAccount) {
            endpoint = `/transaction-history/undo/${selectedAccount.accountNumber}`;
        }
        
        const response = await apiCall(endpoint, 'POST');

        if (response.success) {
            showMessage('message', `✓ ${response.message}`, 'success');
            setTimeout(() => {
                loadHistorySummary();
                loadUndoableTransactions();
                loadRedoableTransactions();
            }, 1000);
        } else {
            showMessage('message', response.message, 'error');
        }
    } catch (error) {
        showMessage('message', 'Undo failed: ' + error.message, 'error');
    }
}

// Handle redo
async function handleRedo() {
    const accountInfo = selectedAccount 
        ? `for account ${selectedAccount.accountNumber} (${selectedAccount.accountType})` 
        : 'across all accounts';
    
    if (!confirm(`Are you sure you want to redo the last undone transaction ${accountInfo}?`)) {
        return;
    }

    try {
        let endpoint = '/transaction-history/redo';
        if (selectedAccount) {
            endpoint = `/transaction-history/redo/${selectedAccount.accountNumber}`;
        }
        
        const response = await apiCall(endpoint, 'POST');

        if (response.success) {
            showMessage('message', `✓ ${response.message}`, 'success');
            setTimeout(() => {
                loadHistorySummary();
                loadUndoableTransactions();
                loadRedoableTransactions();
            }, 1000);
        } else {
            showMessage('message', response.message, 'error');
        }
    } catch (error) {
        showMessage('message', 'Redo failed: ' + error.message, 'error');
    }
}

// Load all data on page load
loadAccounts();
loadHistorySummary();
loadUndoableTransactions();
loadRedoableTransactions();

// Auto-refresh every 10 seconds
setInterval(() => {
    loadHistorySummary();
    loadUndoableTransactions();
    loadRedoableTransactions();
}, 10000);
