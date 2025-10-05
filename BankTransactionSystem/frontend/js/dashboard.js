// Check if user is logged in
if (!requireLogin()) {
    // Redirect handled by requireLogin
}

// Load customer info
const customer = getFromLocalStorage('customer');
if (customer) {
    document.getElementById('customerName').textContent = customer.name;
}

// Load accounts summary
async function loadAccountsSummary() {
    try {
        const accounts = await apiCall(`/accounts/customer/${customer.id}`);
        
        if (accounts && accounts.length > 0) {
            const summaryDiv = document.getElementById('accountsSummary');
            summaryDiv.innerHTML = '<h3>Your Accounts Summary</h3>';
            
            accounts.forEach(account => {
                const accountCard = document.createElement('div');
                accountCard.className = 'account-card';
                accountCard.innerHTML = `
                    <h3>Account: ${account.accountNumber}</h3>
                    <p>Type: ${account.accountType === 'SB' ? 'Savings Bank' : 'Current Account'}</p>
                    <p>Balance: INR ${account.balance.toFixed(2)}</p>
                `;
                summaryDiv.appendChild(accountCard);
            });
        }
    } catch (error) {
        console.error('Error loading accounts:', error);
    }
}

function handleLogout() {
    if (confirm('Are you sure you want to logout?')) {
        clearLocalStorage();
        window.location.href = '../index.html';
    }
}

// Undo last transaction directly from dashboard
async function handleUndoLastTransaction() {
    if (!confirm('Are you sure you want to UNDO your last transaction? This will reverse the most recent successful transaction.')) {
        return;
    }

    try {
        const response = await apiCall('/transaction-history/undo', 'POST');

        if (response.success) {
            alert('✓ Transaction UNDONE successfully!\n\n' + response.message);
            loadAccountsSummary(); // Refresh account balances
        } else {
            alert('✗ Undo failed!\n\n' + response.message);
        }
    } catch (error) {
        alert('✗ Undo failed!\n\n' + error.message);
    }
}

// Redo last undone transaction directly from dashboard
async function handleRedoTransaction() {
    if (!confirm('Are you sure you want to REDO the last undone transaction? This will re-execute the transaction.')) {
        return;
    }

    try {
        const response = await apiCall('/transaction-history/redo', 'POST');

        if (response.success) {
            alert('✓ Transaction REDONE successfully!\n\n' + response.message);
            loadAccountsSummary(); // Refresh account balances
        } else {
            alert('✗ Redo failed!\n\n' + response.message);
        }
    } catch (error) {
        alert('✗ Redo failed!\n\n' + error.message);
    }
}

// Load summary on page load
loadAccountsSummary();
