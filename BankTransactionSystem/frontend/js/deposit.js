if (!requireLogin()) {
    // Redirect handled
}

const customer = getFromLocalStorage('customer');
let selectedAccountNumber = null;

async function loadAccounts() {
    try {
        const accounts = await apiCall(`/accounts/customer/${customer.id}`);
        
        const accountsList = document.getElementById('accountsList');
        accountsList.innerHTML = `
            <h3>Select Account for Deposit</h3>
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
                💳 Selected Account for Deposit
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
                    <span style="font-weight: 600;">Current Balance:</span>
                    <span style="font-size: 18px; font-weight: bold; color: #ffd700;">INR ${account.balance.toFixed(2)}</span>
                </div>
            </div>
            <button onclick="showAllAccountsDeposit()" style="margin-top: 15px; padding: 10px 20px; background: rgba(255,255,255,0.2); color: white; border: 2px solid white; border-radius: 5px; cursor: pointer; font-weight: 600; transition: all 0.3s;">
                ↩️ Change Account
            </button>
        </div>
    `;
    
    // Store accounts for later use
    window.allAccountsDataDeposit = allAccounts;
    
    // Fetch and display transaction limits
    try {
        const limits = await apiCall(`/transactions/limits/${account.accountType}`);
        
        document.getElementById('depositForm').style.display = 'block';
        
        const limitsInfo = `
            <div class="info-message" style="background: #e3f2fd; padding: 15px; border-radius: 8px; margin: 15px 0;">
                <h4 style="margin-top: 0; color: #1976d2;">📊 Transaction Limits for ${limits.accountType}</h4>
                <ul style="margin: 10px 0; padding-left: 20px;">
                    <li><strong>Minimum per transaction:</strong> INR ${limits.minAmount.toFixed(2)}</li>
                    <li><strong>Maximum per transaction:</strong> INR ${limits.maxAmount.toFixed(2)}</li>
                    <li><strong>Daily limit:</strong> INR ${limits.dailyLimit.toFixed(2)}</li>
                </ul>
            </div>
        `;
        
        // Insert limits info before the form
        const form = document.getElementById('depositForm');
        let limitsDiv = document.getElementById('limitsInfo');
        if (!limitsDiv) {
            limitsDiv = document.createElement('div');
            limitsDiv.id = 'limitsInfo';
            form.parentNode.insertBefore(limitsDiv, form);
        }
        limitsDiv.innerHTML = limitsInfo;
        
        showMessage('message', '✓ Account selected successfully! Enter amount below.', 'success');
    } catch (error) {
        document.getElementById('depositForm').style.display = 'block';
        showMessage('message', '✓ Account selected successfully! Enter amount below.', 'success');
    }
}

async function handleDeposit(event) {
    event.preventDefault();

    if (!selectedAccountNumber) {
        showMessage('message', 'Please select an account first', 'error');
        return;
    }

    const amount = parseFloat(document.getElementById('amount').value);

    try {
        const response = await apiCall('/transactions/deposit', 'POST', {
            accountNumber: selectedAccountNumber,
            amount: amount
        });

        if (response.success) {
            // Get updated account details
            const accounts = await apiCall(`/accounts/customer/${customer.id}`);
            const updatedAccount = accounts.find(acc => acc.accountNumber === selectedAccountNumber);
            
            // Show success page
            displayDepositSuccessPage(amount, updatedAccount, response);
        } else {
            showMessage('message', response.message, 'error');
        }
    } catch (error) {
        showMessage('message', 'Deposit failed: ' + error.message, 'error');
    }
}

// Function to show all accounts again when user clicks "Change Account"
function showAllAccountsDeposit() {
    const accountCardsContainer = document.getElementById('accountCardsContainer');
    const allAccounts = window.allAccountsDataDeposit || [];
    
    // Clear selected account
    selectedAccountNumber = null;
    
    // Hide form and limits
    document.getElementById('depositForm').style.display = 'none';
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

function displayDepositSuccessPage(depositAmount, account, transactionResponse) {
    // Hide the form and accounts list
    document.getElementById('accountsList').style.display = 'none';
    document.getElementById('depositForm').style.display = 'none';
    const limitsDiv = document.getElementById('limitsInfo');
    if (limitsDiv) limitsDiv.style.display = 'none';
    
    // Get account type full name
    const accountTypeName = account.accountType === 'SB' ? 'Savings Bank' : 'Current Account';
    
    // Create success message HTML
    const successHTML = `
        <div style="text-align: center; padding: 20px;">
            <div style="background-color: #d4edda; border: 2px solid #28a745; border-radius: 10px; padding: 30px; margin-bottom: 30px;">
                <div style="font-size: 60px; margin-bottom: 15px;">✅</div>
                <h2 style="color: #155724; margin: 0 0 10px 0;">Deposit Successful!</h2>
                <p style="color: #155724; margin: 0;">Your money has been deposited successfully.</p>
            </div>
            
            <div style="background-color: #fff; border: 2px solid #e0e0e0; border-radius: 10px; padding: 25px; margin-bottom: 30px; text-align: left;">
                <h3 style="color: #333; margin-top: 0; text-align: center; border-bottom: 2px solid #4CAF50; padding-bottom: 10px;">
                    💰 Deposit Details
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
                    
                    <div style="display: flex; justify-content: space-between; padding: 12px; background-color: #fff3cd; border-radius: 5px; margin-bottom: 10px; border: 2px solid #ffc107;">
                        <strong style="color: #856404;">Amount Deposited:</strong>
                        <span style="color: #28a745; font-size: 20px; font-weight: bold;">+ INR ${depositAmount.toFixed(2)}</span>
                    </div>
                    
                    <div style="display: flex; justify-content: space-between; padding: 12px; background-color: #d4edda; border-radius: 5px; margin-bottom: 10px; border: 2px solid #28a745;">
                        <strong style="color: #155724;">New Balance:</strong>
                        <span style="color: #155724; font-size: 20px; font-weight: bold;">INR ${account.balance.toFixed(2)}</span>
                    </div>
                    
                    <div style="display: flex; justify-content: space-between; padding: 12px; background-color: #f8f9fa; border-radius: 5px; margin-bottom: 10px;">
                        <strong style="color: #555;">Transaction Status:</strong>
                        <span style="color: #28a745; font-weight: 600;">✓ SUCCESS</span>
                    </div>
                    
                    <div style="display: flex; justify-content: space-between; padding: 12px; background-color: #f8f9fa; border-radius: 5px;">
                        <strong style="color: #555;">Transaction Date:</strong>
                        <span style="color: #333;">${new Date().toLocaleString()}</span>
                    </div>
                </div>
            </div>
            
            <div style="background-color: #e3f2fd; border-left: 4px solid #2196F3; padding: 15px; margin-bottom: 30px; text-align: left;">
                <strong style="color: #1565c0;">ℹ️ Transaction Summary:</strong>
                <ul style="margin: 10px 0 0 20px; color: #1565c0; font-size: 14px;">
                    <li>Previous Balance: INR ${(account.balance - depositAmount).toFixed(2)}</li>
                    <li>Deposited: INR ${depositAmount.toFixed(2)}</li>
                    <li>Current Balance: INR ${account.balance.toFixed(2)}</li>
                    <li>Transaction recorded successfully</li>
                </ul>
            </div>
            
            <button onclick="location.href='customer-dashboard.html'" 
                    style="background-color: #4CAF50; color: white; padding: 15px 40px; font-size: 16px; border: none; border-radius: 5px; cursor: pointer; font-weight: bold; box-shadow: 0 4px 6px rgba(0,0,0,0.1); transition: all 0.3s;">
                🏠 Go Back to Dashboard
            </button>
        </div>
    `;
    
    // Insert the success page in the content area
    const contentArea = document.querySelector('.content');
    contentArea.innerHTML = successHTML;
    
    // Add hover effect to button
    const button = contentArea.querySelector('button');
    button.addEventListener('mouseenter', function() {
        this.style.backgroundColor = '#45a049';
        this.style.transform = 'translateY(-2px)';
        this.style.boxShadow = '0 6px 8px rgba(0,0,0,0.15)';
    });
    button.addEventListener('mouseleave', function() {
        this.style.backgroundColor = '#4CAF50';
        this.style.transform = 'translateY(0)';
        this.style.boxShadow = '0 4px 6px rgba(0,0,0,0.1)';
    });
}

loadAccounts();
