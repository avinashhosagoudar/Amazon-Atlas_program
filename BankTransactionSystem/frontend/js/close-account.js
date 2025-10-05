if (!requireLogin()) {
    // Redirect handled
}

const customer = getFromLocalStorage('customer');
let selectedAccount = null;

async function loadAccounts() {
    try {
        const accounts = await apiCall(`/accounts/customer/${customer.id}`);
        
        const accountsList = document.getElementById('accountsList');
        accountsList.innerHTML = '<h3>Select Account to Close</h3>';
        
        if (accounts && accounts.length > 0) {
            accounts.forEach(account => {
                const accountCard = document.createElement('div');
                accountCard.className = 'account-card';
                accountCard.onclick = () => showCloseConfirmation(account);
                
                const accountTypeName = account.accountType === 'SB' ? 'Savings Bank' : 'Current Account';
                const hasBalance = account.balance > 0;
                
                accountCard.innerHTML = `
                    <h3>Ac No: ${account.accountNumber}</h3>
                    <p><strong>Type:</strong> ${accountTypeName} (${account.accountType})</p>
                    <p><strong>Balance:</strong> INR ${account.balance.toFixed(2)}</p>
                    ${hasBalance 
                        ? '<p style="color: #ff9800;">💰 Balance will be withdrawn before closing</p>' 
                        : '<p style="color: #4caf50;">✓ Ready to close</p>'}
                    <button class="submit-btn" style="margin-top: 10px; background: #d32f2f;">
                        Close This Account
                    </button>
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

function showCloseConfirmation(account) {
    selectedAccount = account;
    
    const modal = document.getElementById('confirmationModal');
    const accountDetails = document.getElementById('accountDetails');
    const balanceWarning = document.getElementById('balanceWarning');
    const withdrawalInfo = document.getElementById('withdrawalInfo');
    const passwordField = document.getElementById('confirmPassword');
    
    // Clear password field
    passwordField.value = '';
    
    // Show account details
    const accountTypeName = account.accountType === 'SB' ? 'Savings Bank' : 'Current Account';
    accountDetails.innerHTML = `
        <p><strong>Account Number:</strong> ${account.accountNumber}</p>
        <p><strong>Account Type:</strong> ${accountTypeName} (${account.accountType})</p>
        <p><strong>Current Balance:</strong> INR ${account.balance.toFixed(2)}</p>
    `;
    
    // Show withdrawal warning if account has balance
    if (account.balance > 0) {
        balanceWarning.style.display = 'block';
        withdrawalInfo.textContent = `Your account has a balance of INR ${account.balance.toFixed(2)}. This amount will be automatically withdrawn and credited to you before the account is closed.`;
    } else {
        balanceWarning.style.display = 'none';
    }
    
    // Show modal
    modal.style.display = 'flex';
}

function cancelClose() {
    selectedAccount = null;
    document.getElementById('confirmationModal').style.display = 'none';
    document.getElementById('confirmPassword').value = '';
}

async function confirmClose() {
    if (!selectedAccount) {
        return;
    }
    
    const passwordField = document.getElementById('confirmPassword');
    const password = passwordField.value;
    
    // Validate password
    if (!password || password.trim() === '') {
        showMessage('message', '🔒 Please enter your password to confirm account closure', 'error');
        passwordField.focus();
        return;
    }
    
    // Get the button that was clicked
    const buttons = document.querySelectorAll('#confirmationModal button');
    const closeBtn = buttons[0]; // First button is "Close Account"
    const originalText = closeBtn.textContent;
    closeBtn.textContent = 'Processing...';
    closeBtn.disabled = true;
    
    try {
        const response = await apiCall('/accounts/close', 'POST', {
            accountNumber: selectedAccount.accountNumber,
            customerId: customer.id,
            password: password
        });

        if (response.success) {
            // Hide modal
            document.getElementById('confirmationModal').style.display = 'none';
            
            // Show success page
            displaySuccessPage(response);
        } else {
            closeBtn.textContent = originalText;
            closeBtn.disabled = false;
            passwordField.value = '';
            showMessage('message', response.message, 'error');
        }
    } catch (error) {
        closeBtn.textContent = originalText;
        closeBtn.disabled = false;
        passwordField.value = '';
        showMessage('message', 'Account closure failed: ' + error.message, 'error');
    }
}

function displaySuccessPage(response) {
    const content = document.querySelector('.content');
    
    const successHTML = `
        <div style="text-align: center; padding: 20px;">
            <div style="background-color: #e8f5e9; border: 2px solid #4caf50; border-radius: 10px; padding: 30px; margin-bottom: 30px;">
                <div style="font-size: 60px; margin-bottom: 15px;">✅</div>
                <h2 style="color: #2e7d32; margin: 0 0 10px 0;">Account Closed Successfully!</h2>
                <p style="color: #2e7d32; margin: 0;">Your account has been permanently closed.</p>
            </div>
            
            <div style="background-color: #fff; border: 2px solid #e0e0e0; border-radius: 10px; padding: 25px; margin-bottom: 30px; text-align: left;">
                <h3 style="color: #333; margin-top: 0; text-align: center; border-bottom: 2px solid #4caf50; padding-bottom: 10px;">
                    📋 Closure Details
                </h3>
                
                <div style="margin: 20px 0;">
                    <div style="display: flex; justify-content: space-between; padding: 12px; background-color: #f8f9fa; border-radius: 5px; margin-bottom: 10px;">
                        <strong style="color: #555;">Account Number:</strong>
                        <span style="font-family: monospace; font-weight: bold;">${response.accountNumber}</span>
                    </div>
                    
                    <div style="display: flex; justify-content: space-between; padding: 12px; background-color: #f8f9fa; border-radius: 5px; margin-bottom: 10px;">
                        <strong style="color: #555;">Status:</strong>
                        <span style="color: #d32f2f; font-weight: bold;">CLOSED</span>
                    </div>
                    
                    ${response.withdrawnAmount > 0 ? `
                        <div style="background: #fff3e0; border: 2px solid #ff9800; border-radius: 8px; padding: 15px; margin: 20px 0;">
                            <h4 style="color: #e65100; margin-top: 0; display: flex; align-items: center;">
                                💰 Automatic Withdrawal Completed
                            </h4>
                            
                            <div style="display: flex; justify-content: space-between; padding: 12px; background-color: white; border-radius: 5px; margin-bottom: 10px;">
                                <strong style="color: #555;">Withdrawn Amount:</strong>
                                <span style="font-size: 18px; font-weight: bold; color: #4caf50;">INR ${response.withdrawnAmount.toFixed(2)}</span>
                            </div>
                            
                            <div style="display: flex; justify-content: space-between; padding: 12px; background-color: white; border-radius: 5px; margin-bottom: 10px;">
                                <strong style="color: #555;">Transaction ID:</strong>
                                <span style="font-family: monospace; font-weight: bold;">${response.transactionId}</span>
                            </div>
                            
                            <p style="margin: 10px 0 0 0; color: #e65100; font-size: 0.9em;">
                                ℹ️ The funds have been withdrawn and are available for your use.
                            </p>
                        </div>
                    ` : '<p style="color: #666; text-align: center; margin: 20px 0;">Account had zero balance. No withdrawal needed.</p>'}
                    
                    <div style="display: flex; justify-content: space-between; padding: 12px; background-color: #f8f9fa; border-radius: 5px; margin-bottom: 10px;">
                        <strong style="color: #555;">Closure Date:</strong>
                        <span>${new Date().toLocaleString()}</span>
                    </div>
                </div>
            </div>
            
            <div style="background: #e3f2fd; border-left: 4px solid #2196f3; padding: 20px; border-radius: 8px; text-align: left; margin-bottom: 30px;">
                <h4 style="color: #1565c0; margin-top: 0;">📌 Important Notes:</h4>
                <ul style="color: #333; margin: 10px 0; padding-left: 20px;">
                    <li>Your account has been permanently closed and cannot be reopened.</li>
                    ${response.withdrawnAmount > 0 
                        ? '<li>The withdrawn amount has been processed and is available.</li>' 
                        : ''}
                    <li>You can create a new account anytime from your dashboard.</li>
                    <li>All account records have been saved in the audit log.</li>
                </ul>
            </div>
            
            <div style="display: flex; gap: 15px; justify-content: center;">
                <button onclick="location.href='customer-dashboard.html'" class="submit-btn" style="background: #667eea; padding: 15px 40px; font-size: 1.1em;">
                    🏠 Go to Dashboard
                </button>
                <button onclick="location.reload()" class="submit-btn" style="background: #757575; padding: 15px 40px; font-size: 1.1em;">
                    Close Another Account
                </button>
            </div>
        </div>
    `;
    
    content.innerHTML = successHTML;
}

// Close modal when clicking outside
document.addEventListener('click', function(event) {
    const modal = document.getElementById('confirmationModal');
    if (event.target === modal) {
        cancelClose();
    }
});

// Load accounts on page load
loadAccounts();
