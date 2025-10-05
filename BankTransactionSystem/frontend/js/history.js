if (!requireLogin()) {
    // Redirect handled
}

const customer = getFromLocalStorage('customer');

async function loadAccounts() {
    try {
        const accounts = await apiCall(`/accounts/customer/${customer.id}`);
        
        const accountsList = document.getElementById('accountsList');
        accountsList.innerHTML = '<h3>Select Account for Transaction History</h3>';
        
        if (accounts && accounts.length > 0) {
            accounts.forEach(account => {
                const accountCard = document.createElement('div');
                accountCard.className = 'account-card';
                accountCard.onclick = () => loadTransactionHistory(account.accountNumber);
                accountCard.innerHTML = `
                    <h3>Ac No: ${account.accountNumber}</h3>
                    <p>Type: ${account.accountType}</p>
                    <p>Click to view transaction history</p>
                `;
                accountsList.appendChild(accountCard);
            });
        } else {
            accountsList.innerHTML += '<p>No accounts found.</p>';
        }
    } catch (error) {
        showMessage('message', 'Error loading accounts: ' + error.message, 'error');
    }
}

async function loadTransactionHistory(accountNumber) {
    try {
        const transactions = await apiCall(`/transactions/history/${accountNumber}`);
        
        const historyDiv = document.getElementById('transactionHistory');
        historyDiv.innerHTML = `<h3>Transaction History for Account: ${accountNumber}</h3>`;
        
        if (transactions && transactions.length > 0) {
            const table = document.createElement('table');
            table.className = 'transaction-table';
            table.innerHTML = `
                <thead>
                    <tr>
                        <th>Transaction ID</th>
                        <th>Type</th>
                        <th>Amount</th>
                        <th>Status</th>
                        <th>Balance After</th>
                        <th>Date</th>
                    </tr>
                </thead>
                <tbody>
                    ${transactions.map(txn => `
                        <tr>
                            <td>${txn.transactionId}</td>
                            <td>${txn.transactionType}</td>
                            <td>INR ${txn.amount.toFixed(2)}</td>
                            <td class="status-${txn.status.toLowerCase()}">${txn.status}</td>
                            <td>INR ${txn.balanceAfter ? txn.balanceAfter.toFixed(2) : 'N/A'}</td>
                            <td>${new Date(txn.transactionDate).toLocaleString()}</td>
                        </tr>
                    `).join('')}
                </tbody>
            `;
            historyDiv.appendChild(table);
        } else {
            historyDiv.innerHTML += '<p>No transactions found for this account.</p>';
        }
    } catch (error) {
        showMessage('message', 'Error loading transaction history: ' + error.message, 'error');
    }
}

loadAccounts();
