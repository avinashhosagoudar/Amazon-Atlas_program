if (!requireLogin()) {
    // Redirect handled
}

const customer = getFromLocalStorage('customer');
let selectedFromAccount = null;

async function loadAccounts() {
    try {
        const accounts = await apiCall(`/accounts/customer/${customer.id}`);
        
        const accountsList = document.getElementById('accountsList');
        accountsList.innerHTML = `
            <h3>Select From Account</h3>
            <div id="accountCardsContainer"></div>
        `;
        
        const accountCardsContainer = document.getElementById('accountCardsContainer');
        
        if (accounts && accounts.length > 0) {
            accounts.forEach(account => {
                const accountCard = document.createElement('div');
                accountCard.className = 'account-card';
                accountCard.onclick = () => selectFromAccount(account, accounts);
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

async function selectFromAccount(account, allAccounts) {
    selectedFromAccount = account.accountNumber;
    
    // Replace the account cards container with only the selected account
    const accountCardsContainer = document.getElementById('accountCardsContainer');
    const accountTypeName = account.accountType === 'SB' ? 'Savings Bank' : 'Current Account';
    
    accountCardsContainer.innerHTML = `
        <div style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding: 20px; border-radius: 10px; color: white; margin-bottom: 20px;">
            <h3 style="margin: 0 0 15px 0; color: white; border-bottom: 2px solid rgba(255,255,255,0.3); padding-bottom: 10px;">
                💳 Selected Account for Transfer (From)
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
            <button onclick="showAllAccountsTransfer()" style="margin-top: 15px; padding: 10px 20px; background: rgba(255,255,255,0.2); color: white; border: 2px solid white; border-radius: 5px; cursor: pointer; font-weight: 600; transition: all 0.3s;">
                ↩️ Change Account
            </button>
        </div>
    `;
    
    // Store accounts for later use
    window.allAccountsDataTransfer = allAccounts;
    
    document.getElementById('selectedAccount').textContent = account.accountNumber;
    
    // Fetch and display transaction limits
    try {
        const limits = await apiCall(`/transactions/limits/${account.accountType}`);
        
        document.getElementById('transferForm').style.display = 'block';
        
        const limitsInfo = `
            <div class="info-message" style="background: #e8f5e9; padding: 15px; border-radius: 8px; margin: 15px 0;">
                <h4 style="margin-top: 0; color: #388e3c;">📊 Transaction Limits for ${limits.accountType}</h4>
                <ul style="margin: 10px 0; padding-left: 20px;">
                    <li><strong>Minimum per transaction:</strong> INR ${limits.minAmount.toFixed(2)}</li>
                    <li><strong>Maximum per transaction:</strong> INR ${limits.maxAmount.toFixed(2)}</li>
                    <li><strong>Daily limit:</strong> INR ${limits.dailyLimit.toFixed(2)}</li>
                </ul>
                <p style="margin: 5px 0; font-size: 0.9em; color: #2e7d32;"><strong>Note:</strong> Daily limit applies to withdrawals and transfers combined.</p>
            </div>
        `;
        
        // Insert limits info before the form
        const form = document.getElementById('transferForm');
        let limitsDiv = document.getElementById('limitsInfo');
        if (!limitsDiv) {
            limitsDiv = document.createElement('div');
            limitsDiv.id = 'limitsInfo';
            form.parentNode.insertBefore(limitsDiv, form);
        }
        limitsDiv.innerHTML = limitsInfo;
    } catch (error) {
        document.getElementById('transferForm').style.display = 'block';
    }
}

// Function to show all accounts again when user clicks "Change Account"
function showAllAccountsTransfer() {
    const accountCardsContainer = document.getElementById('accountCardsContainer');
    const allAccounts = window.allAccountsDataTransfer || [];
    
    // Clear selected account
    selectedFromAccount = null;
    document.getElementById('selectedAccount').textContent = '';
    
    // Hide form and limits
    document.getElementById('transferForm').style.display = 'none';
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
            accountCard.onclick = () => selectFromAccount(account, allAccounts);
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

async function handleTransfer(event) {
    event.preventDefault();

    if (!selectedFromAccount) {
        showMessage('message', 'Please select a from account first', 'error');
        return;
    }

    const toAccount = document.getElementById('toAccount').value;
    const amount = parseFloat(document.getElementById('amount').value);
    const passwordField = document.getElementById('password');
    
    // Check if password field exists and has value
    if (!passwordField) {
        showMessage('message', 'Password field not found. Please refresh the page.', 'error');
        return;
    }
    
    const password = passwordField.value;
    
    if (!password || password.trim() === '') {
        showMessage('message', '🔒 Please enter your password to confirm transfer', 'error');
        passwordField.focus();
        return;
    }

    if (selectedFromAccount === toAccount) {
        showMessage('message', 'Cannot transfer to the same account!', 'error');
        return;
    }

    // Show loading state
    const submitBtn = event.target.querySelector('button[type="submit"]');
    const originalBtnText = submitBtn.textContent;
    submitBtn.textContent = 'Verifying...';
    submitBtn.disabled = true;

    try {
        const response = await apiCall('/transactions/transfer', 'POST', {
            fromAccountNumber: selectedFromAccount,
            toAccountNumber: toAccount,
            amount: amount,
            password: password
        });

        if (response.success) {
            // Get updated account details
            const accounts = await apiCall(`/accounts/customer/${customer.id}`);
            const updatedAccount = accounts.find(acc => 
                acc.accountNumber === selectedFromAccount
            );
            
            // Show success page
            displayTransferSuccessPage(amount, toAccount, updatedAccount, response);
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
        showMessage('message', 'Transfer failed: ' + error.message, 'error');
    }
}

function displayTransferSuccessPage(transferAmount, toAccountNumber, fromAccount, transactionResponse) {
    // Completely hide all form elements
    const accountsList = document.getElementById('accountsList');
    const transferForm = document.getElementById('transferForm');
    const limitsDiv = document.getElementById('limitsInfo');
    const messageDiv = document.getElementById('message');
    
    if (accountsList) accountsList.style.display = 'none';
    if (transferForm) transferForm.style.display = 'none';
    if (limitsDiv) limitsDiv.style.display = 'none';
    if (messageDiv) messageDiv.innerHTML = '';
    
    // Get account type full name
    const accountTypeName = fromAccount.accountType === 'SB' 
        ? 'Savings Bank' 
        : 'Current Account';
    
    // Calculate previous balance (before transfer)
    const previousBalance = fromAccount.balance + transferAmount;
    
    // Create success message HTML with clean design
    const successHTML = `
        <div style="max-width: 800px; margin: 0 auto;">
            <!-- Success Header -->
            <div style="background: linear-gradient(135deg, #28a745 0%, #20c997 100%); 
                        border-radius: 15px; padding: 40px; text-align: center; 
                        margin-bottom: 30px; box-shadow: 0 8px 20px rgba(40, 167, 69, 0.3);">
                <div style="font-size: 80px; margin-bottom: 15px; animation: scaleIn 0.5s ease-out;">✅</div>
                <h2 style="color: white; margin: 0 0 10px 0; font-size: 32px; font-weight: 700;">
                    Transfer Successful!
                </h2>
                <p style="color: rgba(255, 255, 255, 0.95); font-size: 18px; margin: 0;">
                    Your money has been transferred successfully to the destination account.
                </p>
            </div>
            
            <!-- Transfer Details Card -->
            <div style="background: #ffffff; border-radius: 15px; padding: 35px; 
                        margin-bottom: 30px; box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);">
                <h3 style="color: #667eea; margin: 0 0 25px 0; padding-bottom: 15px; 
                           border-bottom: 3px solid #667eea; font-size: 24px; display: flex; 
                           align-items: center; gap: 10px;">
                    <span>💸</span> Transfer Details
                </h3>
                
                <div style="display: grid; gap: 18px;">
                    <!-- Transaction ID -->
                    <div style="padding: 15px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); 
                                border-radius: 10px; color: white;">
                        <div style="font-size: 13px; opacity: 0.9; margin-bottom: 5px;">Transaction ID</div>
                        <div style="font-family: 'Courier New', monospace; font-size: 18px; font-weight: bold; 
                                    letter-spacing: 1px;">
                            ${transactionResponse.transaction.transactionId}
                        </div>
                    </div>
                    
                    <!-- From Account -->
                    <div style="display: flex; justify-content: space-between; align-items: center; 
                                padding: 15px; background-color: #f8f9fa; border-radius: 10px;">
                        <strong style="color: #495057; font-size: 16px;">From Account</strong>
                        <span style="font-family: 'Courier New', monospace; color: #212529; 
                                     font-size: 16px; font-weight: 600;">
                            ${fromAccount.accountNumber}
                        </span>
                    </div>
                    
                    <!-- To Account -->
                    <div style="display: flex; justify-content: space-between; align-items: center; 
                                padding: 15px; background-color: #ffffff; border: 2px solid #e9ecef; border-radius: 10px;">
                        <strong style="color: #495057; font-size: 16px;">To Account</strong>
                        <span style="font-family: 'Courier New', monospace; color: #212529; 
                                     font-size: 16px; font-weight: 600;">
                            ${toAccountNumber}
                        </span>
                    </div>
                    
                    <!-- Account Type -->
                    <div style="display: flex; justify-content: space-between; align-items: center; 
                                padding: 15px; background-color: #f8f9fa; border-radius: 10px;">
                        <strong style="color: #495057; font-size: 16px;">Account Type</strong>
                        <span style="color: #212529; font-size: 16px; font-weight: 600;">
                            ${accountTypeName} (${fromAccount.accountType})
                        </span>
                    </div>
                    
                    <!-- Amount Transferred -->
                    <div style="padding: 20px; background: linear-gradient(135deg, #dc3545 0%, #ff6b6b 100%); 
                                border-radius: 10px; text-align: center; color: white;">
                        <div style="font-size: 14px; opacity: 0.95; margin-bottom: 8px; font-weight: 500;">
                            Amount Transferred
                        </div>
                        <div style="font-size: 36px; font-weight: 700; letter-spacing: 1px;">
                            - INR ${transferAmount.toFixed(2)}
                        </div>
                    </div>
                    
                    <!-- Remaining Balance -->
                    <div style="padding: 20px; background: linear-gradient(135deg, #28a745 0%, #20c997 100%); 
                                border-radius: 10px; text-align: center; color: white;">
                        <div style="font-size: 14px; opacity: 0.95; margin-bottom: 8px; font-weight: 500;">
                            Remaining Balance
                        </div>
                        <div style="font-size: 36px; font-weight: 700; letter-spacing: 1px;">
                            INR ${fromAccount.balance.toFixed(2)}
                        </div>
                    </div>
                    
                    <!-- Transaction Status -->
                    <div style="display: flex; justify-content: space-between; align-items: center; 
                                padding: 15px; background-color: #d4edda; border: 2px solid #28a745; border-radius: 10px;">
                        <strong style="color: #155724; font-size: 16px;">Transaction Status</strong>
                        <span style="color: #155724; font-size: 16px; font-weight: 700;">
                            ✓ ${transactionResponse.transaction.status}
                        </span>
                    </div>
                    
                    <!-- Transaction Date -->
                    <div style="display: flex; justify-content: space-between; align-items: center; 
                                padding: 15px; background-color: #f8f9fa; border-radius: 10px;">
                        <strong style="color: #495057; font-size: 16px;">Transaction Date</strong>
                        <span style="color: #212529; font-size: 16px; font-weight: 600;">
                            ${new Date(transactionResponse.transaction.transactionDate).toLocaleString('en-IN', {
                                day: '2-digit',
                                month: 'short',
                                year: 'numeric',
                                hour: '2-digit',
                                minute: '2-digit',
                                second: '2-digit',
                                hour12: true
                            })}
                        </span>
                    </div>
                </div>
            </div>
            
            <!-- Transaction Summary Info -->
            <div style="background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 100%); 
                        border-left: 5px solid #2196F3; padding: 20px; margin-bottom: 30px; 
                        border-radius: 10px; box-shadow: 0 2px 8px rgba(33, 150, 243, 0.2);">
                <div style="display: flex; align-items: center; gap: 10px; margin-bottom: 15px;">
                    <span style="font-size: 24px;">ℹ️</span>
                    <strong style="color: #1976d2; font-size: 18px;">Transaction Summary</strong>
                </div>
                <ul style="margin: 0; padding-left: 25px; color: #1565c0; line-height: 1.8; font-size: 15px;">
                    <li><strong>Previous Balance:</strong> INR ${previousBalance.toFixed(2)}</li>
                    <li><strong>Amount Transferred:</strong> INR ${transferAmount.toFixed(2)}</li>
                    <li><strong>Remaining Balance:</strong> INR ${fromAccount.balance.toFixed(2)}</li>
                    <li><strong>Transaction Status:</strong> Recorded successfully</li>
                </ul>
            </div>
            
            <!-- Action Button -->
            <div style="text-align: center; margin-top: 35px;">
                <button onclick="location.href='customer-dashboard.html'" 
                        style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); 
                               color: white; padding: 18px 50px; border: none; 
                               border-radius: 10px; font-size: 18px; cursor: pointer; 
                               font-weight: 700; box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4); 
                               transition: all 0.3s ease; display: inline-flex; 
                               align-items: center; gap: 12px;"
                        onmouseover="this.style.transform='translateY(-3px)'; this.style.boxShadow='0 8px 25px rgba(102, 126, 234, 0.5)';"
                        onmouseout="this.style.transform='translateY(0)'; this.style.boxShadow='0 6px 20px rgba(102, 126, 234, 0.4)';">
                    <span style="font-size: 24px;">🏠</span>
                    <span>Go Back to Dashboard</span>
                </button>
            </div>
        </div>
        
        <style>
            @keyframes scaleIn {
                0% {
                    transform: scale(0);
                    opacity: 0;
                }
                50% {
                    transform: scale(1.1);
                }
                100% {
                    transform: scale(1);
                    opacity: 1;
                }
            }
        </style>
    `;
    
    // Replace entire content area with success page
    const contentArea = document.querySelector('.content');
    contentArea.innerHTML = successHTML;
    
    // Scroll to top smoothly
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

loadAccounts();
