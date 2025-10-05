if (!requireLogin()) {
    // Redirect handled
}

const customer = getFromLocalStorage('customer');
let selectedAccountNumber = null;

// Load accounts list
async function loadAccounts() {
    try {
        const accounts = await apiCall(`/accounts/customer/${customer.id}`);
        
        const accountsList = document.getElementById('accountsList');
        accountsList.innerHTML = `
            <h3>Select Account for Withdrawal</h3>
            <div id="accountCardsContainer"></div>
        `;
        
        const accountCardsContainer = document.getElementById('accountCardsContainer');
        
        if (accounts && accounts.length > 0) {
            accounts.forEach(account => {
                const accountCard = document.createElement('div');
                accountCard.className = 'account-card';
                accountCard.onclick = () => selectAccount(account, accounts);
                accountCard.innerHTML = `
                    <h3>Ac No: ${account.accountNumber}</h3>
                    <p>Type: ${account.accountType}</p>
                    <p>Balance: INR ${account.balance.toFixed(2)}</p>
                `;
                accountCardsContainer.appendChild(accountCard);
            });
        } else {
            accountCardsContainer.innerHTML = '<p>No accounts found.</p>';
        }
    } catch (error) {
        showMessage('message', 'Error loading accounts: ' + error.message, 'error');
    }
}

async function selectAccount(account, allAccounts) {
    selectedAccountNumber = account.accountNumber;
    
    // Replace the account cards container with only the selected account
    const accountCardsContainer = document.getElementById('accountCardsContainer');
    const accountTypeName = account.accountType === 'SB' ? 'Savings Bank' : 'Current Account';
    
    accountCardsContainer.innerHTML = `
        <div style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding: 20px; border-radius: 10px; color: white; margin-bottom: 20px;">
            <h3 style="margin: 0 0 15px 0; color: white; border-bottom: 2px solid rgba(255,255,255,0.3); padding-bottom: 10px;">
                💳 Selected Account for Withdrawal
            </h3>
            <div style="background: rgba(255,255,255,0.1); padding: 15px; border-radius: 8px; backdrop-filter: blur(10px);">
                <div style="display: flex; justify-content: space-between; margin-bottom: 10px;">
                    <span style="font-weight: 600;">Account Number:</span>
                    <span style="font-family: monospace; font-size: 16px; font-weight: bold;">${account.accountNumber}</span>
                </div>
                <div style="display: flex; justify-content: space-between; margin-bottom: 10px;">
                    <span style="font-weight: 600;">Account Type:</span>
                    <span style="font-weight: bold;">${accountTypeName} (${account.accountType})</span>
                </div>
                <div style="display: flex; justify-content: space-between;">
                    <span style="font-weight: 600;">Available Balance:</span>
                    <span style="font-size: 18px; font-weight: bold; color: #ffd700;">INR ${account.balance.toFixed(2)}</span>
                </div>
            </div>
            <button onclick="showAllAccounts()" style="margin-top: 15px; padding: 10px 20px; background: rgba(255,255,255,0.2); color: white; border: 2px solid white; border-radius: 5px; cursor: pointer; font-weight: 600; transition: all 0.3s;">
                ↩️ Change Account
            </button>
        </div>
    `;
    
    // Store accounts for later use
    window.allAccountsData = allAccounts;
    
    // Fetch and display transaction limits
    try {
        const limits = await apiCall(`/transactions/limits/${account.accountType}`);
        
        document.getElementById('withdrawForm').style.display = 'block';
        
        const limitsInfo = `
            <div class="info-message" style="background: #fff3e0; padding: 15px; border-radius: 8px; margin: 15px 0;">
                <h4 style="margin-top: 0; color: #f57c00;">📊 Transaction Limits for ${limits.accountType}</h4>
                <ul style="margin: 10px 0; padding-left: 20px;">
                    <li><strong>Minimum per transaction:</strong> INR ${limits.minAmount.toFixed(2)}</li>
                    <li><strong>Maximum per transaction:</strong> INR ${limits.maxAmount.toFixed(2)}</li>
                    <li><strong>Daily limit:</strong> INR ${limits.dailyLimit.toFixed(2)}</li>
                </ul>
                <p style="margin: 5px 0; font-size: 0.9em; color: #e65100;"><strong>Note:</strong> Daily limit applies to withdrawals and transfers combined.</p>
            </div>
        `;
        
        // Insert limits info before the form
        const form = document.getElementById('withdrawForm');
        let limitsDiv = document.getElementById('limitsInfo');
        if (!limitsDiv) {
            limitsDiv = document.createElement('div');
            limitsDiv.id = 'limitsInfo';
            form.parentNode.insertBefore(limitsDiv, form);
        }
        limitsDiv.innerHTML = limitsInfo;
        
        showMessage('message', '✓ Account selected successfully! Enter amount below.', 'success');
    } catch (error) {
        document.getElementById('withdrawForm').style.display = 'block';
        showMessage('message', '✓ Account selected successfully! Enter amount below.', 'success');
    }
}

// Function to show all accounts again when user clicks "Change Account"
function showAllAccounts() {
    const accountCardsContainer = document.getElementById('accountCardsContainer');
    const allAccounts = window.allAccountsData || [];
    
    // Clear selected account
    selectedAccountNumber = null;
    
    // Hide form and limits
    document.getElementById('withdrawForm').style.display = 'none';
    const limitsDiv = document.getElementById('limitsInfo');
    if (limitsDiv) limitsDiv.remove();
    
    // Clear messages
    document.getElementById('message').innerHTML = '';
    
    // Redisplay all account cards
    accountCardsContainer.innerHTML = '';
    
    if (allAccounts && allAccounts.length > 0) {
        allAccounts.forEach(account => {
            const accountCard = document.createElement('div');
            accountCard.className = 'account-card';
            accountCard.onclick = () => selectAccount(account, allAccounts);
            accountCard.innerHTML = `
                <h3>Ac No: ${account.accountNumber}</h3>
                <p>Type: ${account.accountType}</p>
                <p>Balance: INR ${account.balance.toFixed(2)}</p>
            `;
            accountCardsContainer.appendChild(accountCard);
        });
    } else {
        accountCardsContainer.innerHTML = '<p>No accounts found.</p>';
    }
}

async function handleWithdraw(event) {
    event.preventDefault();

    if (!selectedAccountNumber) {
        showMessage('message', 'Please select an account first', 'error');
        return;
    }

    const amount = parseFloat(document.getElementById('amount').value);
    const passwordField = document.getElementById('password');
    
    // Check if password field exists and has value
    if (!passwordField) {
        showMessage('message', 'Password field not found. Please refresh the page.', 'error');
        return;
    }
    
    const password = passwordField.value;
    
    if (!password || password.trim() === '') {
        showMessage('message', '🔒 Please enter your password to confirm withdrawal', 'error');
        passwordField.focus();
        return;
    }

    // Show loading state
    const submitBtn = event.target.querySelector('button[type="submit"]');
    const originalBtnText = submitBtn.textContent;
    submitBtn.textContent = 'Verifying...';
    submitBtn.disabled = true;

    try {
        const response = await apiCall('/transactions/withdraw', 'POST', {
            accountNumber: selectedAccountNumber,
            amount: amount,
            password: password
        });

        if (response.success) {
            // Get updated account details
            const accounts = await apiCall(`/accounts/customer/${customer.id}`);
            const updatedAccount = accounts.find(acc => acc.accountNumber === selectedAccountNumber);
            
            // Show success page
            displayWithdrawSuccessPage(amount, updatedAccount, response);
        } else {
            submitBtn.textContent = originalBtnText;
            submitBtn.disabled = false;
            // Clear password field on error
            document.getElementById('password').value = '';
            showMessage('message', response.message, 'error');
        }
    } catch (error) {
        submitBtn.textContent = originalBtnText;
        submitBtn.disabled = false;
        // Clear password field on error
        document.getElementById('password').value = '';
        showMessage('message', 'Withdrawal failed: ' + error.message, 'error');
    }
}

function displayWithdrawSuccessPage(withdrawAmount, account, transactionResponse) {
    // Hide the form and accounts list
    document.getElementById('accountsList').style.display = 'none';
    document.getElementById('withdrawForm').style.display = 'none';
    const limitsDiv = document.getElementById('limitsInfo');
    if (limitsDiv) limitsDiv.style.display = 'none';
    
    // Get account type full name
    const accountTypeName = account.accountType === 'SB' ? 'Savings Bank' : 'Current Account';
    
    // Create success message HTML
    const successHTML = `
        <div style="text-align: center; padding: 20px;">
            <div style="background-color: #fff3e0; border: 2px solid #ff9800; border-radius: 10px; padding: 30px; margin-bottom: 30px;">
                <div style="font-size: 60px; margin-bottom: 15px;">✅</div>
                <h2 style="color: #e65100; margin: 0 0 10px 0;">Withdrawal Successful!</h2>
                <p style="color: #e65100; margin: 0;">Your money has been withdrawn successfully.</p>
            </div>
            
            <div style="background-color: #fff; border: 2px solid #e0e0e0; border-radius: 10px; padding: 25px; margin-bottom: 30px; text-align: left;">
                <h3 style="color: #333; margin-top: 0; text-align: center; border-bottom: 2px solid #ff9800; padding-bottom: 10px;">
                    💰 Withdrawal Details
                </h3>
                
                <div style="margin: 20px 0;">
                    <div style="display: flex; justify-content: space-between; padding: 12px; background-color: #f8f9fa; border-radius: 5px; margin-bottom: 10px;">
                        <strong style="color: #555;">Transaction ID:</strong>
                        <span style="color: #2196F3; font-family: monospace; font-size: 14px; font-weight: bold;">${transactionResponse.transaction.transactionId}</span>
                    </div>
                    
                    <div style="display: flex; justify-content: space-between; padding: 12px; background-color: #f8f9fa; border-radius: 5px; margin-bottom: 10px;">
                        <strong style="color: #555;">Account Number:</strong>
                        <span style="color: #333; font-family: monospace; font-weight: 600;">${account.accountNumber}</span>
                    </div>
                    
                    <div style="display: flex; justify-content: space-between; padding: 12px; background-color: #f8f9fa; border-radius: 5px; margin-bottom: 10px;">
                        <strong style="color: #555;">Account Type:</strong>
                        <span style="color: #333; font-weight: 600;">${accountTypeName} (${account.accountType})</span>
                    </div>
                    
                    <div style="display: flex; justify-content: space-between; padding: 12px; background-color: #ffebee; border-radius: 5px; margin-bottom: 10px; border: 2px solid #f44336;">
                        <strong style="color: #c62828;">Amount Withdrawn:</strong>
                        <span style="color: #d32f2f; font-size: 20px; font-weight: bold;">- INR ${withdrawAmount.toFixed(2)}</span>
                    </div>
                    
                    <div style="display: flex; justify-content: space-between; padding: 12px; background-color: #e3f2fd; border-radius: 5px; margin-bottom: 10px; border: 2px solid #2196F3;">
                        <strong style="color: #1565c0;">New Balance:</strong>
                        <span style="color: #1565c0; font-size: 20px; font-weight: bold;">INR ${account.balance.toFixed(2)}</span>
                    </div>
                    
                    <div style="display: flex; justify-content: space-between; padding: 12px; background-color: #f8f9fa; border-radius: 5px;">
                        <strong style="color: #555;">Date & Time:</strong>
                        <span style="color: #333; font-weight: 600;">${new Date().toLocaleString()}</span>
                    </div>
                </div>
            </div>
            
            <div style="display: flex; gap: 15px; justify-content: center; margin-top: 30px;">
                <button onclick="window.print()" style="padding: 12px 30px; background-color: #2196F3; color: white; border: none; border-radius: 5px; font-size: 16px; cursor: pointer; font-weight: 600;">
                    🖨️ Print Receipt
                </button>
                <button onclick="location.reload()" style="padding: 12px 30px; background-color: #ff9800; color: white; border: none; border-radius: 5px; font-size: 16px; cursor: pointer; font-weight: 600;">
                    ↩️ Make Another Withdrawal
                </button>
                <button onclick="window.location.href='customer-dashboard.html'" style="padding: 12px 30px; background-color: #4CAF50; color: white; border: none; border-radius: 5px; font-size: 16px; cursor: pointer; font-weight: 600;">
                    🏠 Back to Dashboard
                </button>
            </div>
        </div>
    `;
    
    // Display the success page
    const messageDiv = document.getElementById('message');
    messageDiv.innerHTML = successHTML;
    messageDiv.style.display = 'block';
    messageDiv.className = '';
}

// Load accounts on page load
loadAccounts();
