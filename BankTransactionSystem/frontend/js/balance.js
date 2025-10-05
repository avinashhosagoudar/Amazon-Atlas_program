if (!requireLogin()) {
    // Redirect handled
}

const customer = getFromLocalStorage('customer');
let selectedAccount = null;

// Load accounts list
async function loadAccounts() {
    try {
        const accounts = await apiCall(`/accounts/customer/${customer.id}`);
        
        const accountsList = document.getElementById('accountsList');
        accountsList.innerHTML = '<h3>Your Accounts</h3>';
        
        if (accounts && accounts.length > 0) {
            accounts.forEach(account => {
                const accountCard = document.createElement('div');
                accountCard.className = 'account-card';
                accountCard.onclick = () => selectAccount(account);
                accountCard.innerHTML = `
                    <h3>🏦 Account: ${account.accountNumber}</h3>
                    <p><strong>Type:</strong> ${account.accountType === 'SB' ? 'Savings Bank' : 'Current Account'}</p>
                    <p class="click-hint">🔒 Click to view balance (password required)</p>
                `;
                accountsList.appendChild(accountCard);
            });
        } else {
            accountsList.innerHTML += '<p>No accounts found. Please create an account first.</p>';
        }
    } catch (error) {
        showMessage('message', 'Error loading accounts: ' + error.message, 'error');
    }
}

function selectAccount(account) {
    selectedAccount = account;
    
    // Show password prompt
    const balanceDetails = document.getElementById('balanceDetails');
    balanceDetails.innerHTML = `
        <div class="password-prompt">
            <h3>🔒 Authentication Required</h3>
            <p>Enter your password to view balance for account: <strong>${account.accountNumber}</strong></p>
            <form id="passwordForm" onsubmit="verifyPasswordAndShowBalance(event)">
                <div class="form-group">
                    <label for="password">Password:</label>
                    <input type="password" id="password" required>
                </div>
                <button type="submit" class="btn-primary">View Balance</button>
                <button type="button" class="btn-secondary" onclick="cancelBalanceView()">Cancel</button>
            </form>
        </div>
    `;
}

async function verifyPasswordAndShowBalance(event) {
    event.preventDefault();
    
    if (!selectedAccount) {
        showMessage('message', 'No account selected!', 'error');
        return;
    }
    
    const password = document.getElementById('password').value;
    
    try {
        // Verify password by attempting login
        const verifiedCustomer = await apiCall('/customers/login', 'POST', {
            email: customer.email,
            password: password
        });
        
        if (verifiedCustomer && verifiedCustomer.id) {
            // Password correct, show balance
            showBalance(selectedAccount);
            showMessage('message', '✓ Authentication successful!', 'success');
        } else {
            showMessage('message', '❌ Incorrect password! Access denied.', 'error');
        }
    } catch (error) {
        showMessage('message', '❌ Incorrect password! Access denied.', 'error');
        
        // Clear password field
        document.getElementById('password').value = '';
    }
}

function showBalance(account) {
    const balanceDetails = document.getElementById('balanceDetails');
    balanceDetails.innerHTML = `
        <div class="balance-display">
            <h3>✓ Balance Details</h3>
            <div class="balance-info">
                <p><strong>Account Number:</strong> ${account.accountNumber}</p>
                <p><strong>Account Type:</strong> ${account.accountType === 'SB' ? 'Savings Bank' : 'Current Account'}</p>
                <p class="balance-highlight"><strong>Current Balance:</strong> <span class="balance-amount">INR ${account.balance.toFixed(2)}</span></p>
            </div>
            <button class="btn-secondary" onclick="hideBalance()">Hide Balance</button>
        </div>
    `;
}

function hideBalance() {
    selectedAccount = null;
    const balanceDetails = document.getElementById('balanceDetails');
    balanceDetails.innerHTML = '';
    showMessage('message', 'Balance hidden. Select an account to view balance again.', 'success');
}

function cancelBalanceView() {
    selectedAccount = null;
    const balanceDetails = document.getElementById('balanceDetails');
    balanceDetails.innerHTML = '';
    showMessage('message', 'Balance viewing cancelled.', 'success');
}

// Load accounts on page load
loadAccounts();
