if (!requireLogin()) {
    // Redirect handled
}

const customer = getFromLocalStorage('customer');
let existingAccountTypes = [];

// Load existing accounts when page loads
async function loadExistingAccounts() {
    try {
        const accounts = await apiCall(`/accounts/customer/${customer.id}`);
        existingAccountTypes = accounts.map(acc => acc.accountType);
        
        // Display existing accounts
        const existingAccountsDiv = document.getElementById('existingAccounts');
        if (accounts && accounts.length > 0) {
            let html = '<div style="background-color: #fff3cd; border-left: 4px solid #ffc107; padding: 12px; margin-bottom: 15px;">';
            html += '<strong>📋 Your Existing Accounts:</strong><ul style="margin: 8px 0 0 20px;">';
            accounts.forEach(acc => {
                const typeName = acc.accountType === 'SB' ? 'Savings Bank' : 'Current Account';
                html += `<li>${typeName} (${acc.accountNumber}) - Balance: INR ${acc.balance.toFixed(2)}</li>`;
            });
            html += '</ul></div>';
            existingAccountsDiv.innerHTML = html;
            
            // Disable account type options that already exist
            updateAccountTypeOptions();
        }
    } catch (error) {
        console.error('Error loading existing accounts:', error);
    }
}

// Update account type dropdown based on existing accounts
function updateAccountTypeOptions() {
    const accountTypeSelect = document.getElementById('accountType');
    const sbOption = accountTypeSelect.querySelector('option[value="SB"]');
    const caOption = accountTypeSelect.querySelector('option[value="CA"]');
    
    if (existingAccountTypes.includes('SB')) {
        sbOption.disabled = true;
        sbOption.text = 'SB (Savings Bank) - Already exists ✓';
    }
    
    if (existingAccountTypes.includes('CA')) {
        caOption.disabled = true;
        caOption.text = 'CA (Current Account) - Already exists ✓';
    }
    
    // If both account types exist, show message and disable form
    if (existingAccountTypes.includes('SB') && existingAccountTypes.includes('CA')) {
        const form = document.getElementById('createAccountForm');
        form.style.display = 'none';
        showMessage('message', 
            '✓ You already have both account types (SB and CA). No more accounts can be created.', 
            'success');
    }
}

async function handleCreateAccount(event) {
    event.preventDefault();

    const accountType = document.getElementById('accountType').value;
    const initialDeposit = parseFloat(document.getElementById('initialDeposit').value);

    // Validate minimum balance
    const minBalance = accountType === 'SB' ? 1000 : 5000;
    if (initialDeposit < minBalance) {
        showMessage('message', `Minimum initial deposit for ${accountType} account is INR ${minBalance}`, 'error');
        return;
    }

    try {
        const response = await apiCall('/accounts/create', 'POST', {
            customerId: customer.id,
            accountType: accountType,
            initialDeposit: initialDeposit
        });

        // Show success page with account details
        displaySuccessPage(response);
    } catch (error) {
        showMessage('message', 'Account creation failed: ' + error.message, 'error');
    }
}

function displaySuccessPage(account) {
    // Hide the form and existing content
    document.querySelector('.form-container').innerHTML = '';
    
    // Get account type full name
    const accountTypeName = account.accountType === 'SB' ? 'Savings Bank' : 'Current Account';
    
    // Create success message HTML
    const successHTML = `
        <div style="text-align: center; padding: 20px;">
            <div style="background-color: #d4edda; border: 2px solid #28a745; border-radius: 10px; padding: 30px; margin-bottom: 30px;">
                <div style="font-size: 60px; margin-bottom: 15px;">✅</div>
                <h2 style="color: #155724; margin: 0 0 10px 0;">Account Created Successfully!</h2>
                <p style="color: #155724; margin: 0;">Your new ${accountTypeName} account has been created.</p>
            </div>
            
            <div style="background-color: #fff; border: 2px solid #e0e0e0; border-radius: 10px; padding: 25px; margin-bottom: 30px; text-align: left;">
                <h3 style="color: #333; margin-top: 0; text-align: center; border-bottom: 2px solid #4CAF50; padding-bottom: 10px;">
                    📋 Account Details
                </h3>
                
                <div style="margin: 20px 0;">
                    <div style="display: flex; justify-content: space-between; padding: 12px; background-color: #f8f9fa; border-radius: 5px; margin-bottom: 10px;">
                        <strong style="color: #555;">Account Number:</strong>
                        <span style="color: #2196F3; font-family: monospace; font-size: 16px; font-weight: bold;">${account.accountNumber}</span>
                    </div>
                    
                    <div style="display: flex; justify-content: space-between; padding: 12px; background-color: #f8f9fa; border-radius: 5px; margin-bottom: 10px;">
                        <strong style="color: #555;">Account Type:</strong>
                        <span style="color: #333; font-weight: 600;">${accountTypeName} (${account.accountType})</span>
                    </div>
                    
                    <div style="display: flex; justify-content: space-between; padding: 12px; background-color: #f8f9fa; border-radius: 5px; margin-bottom: 10px;">
                        <strong style="color: #555;">Initial Balance:</strong>
                        <span style="color: #28a745; font-size: 18px; font-weight: bold;">INR ${account.balance.toFixed(2)}</span>
                    </div>
                    
                    <div style="display: flex; justify-content: space-between; padding: 12px; background-color: #f8f9fa; border-radius: 5px; margin-bottom: 10px;">
                        <strong style="color: #555;">Account Status:</strong>
                        <span style="color: #28a745; font-weight: 600;">✓ Active</span>
                    </div>
                    
                    <div style="display: flex; justify-content: space-between; padding: 12px; background-color: #f8f9fa; border-radius: 5px;">
                        <strong style="color: #555;">Created On:</strong>
                        <span style="color: #333;">${new Date().toLocaleString()}</span>
                    </div>
                </div>
            </div>
            
            <div style="background-color: #fff3cd; border-left: 4px solid #ffc107; padding: 15px; margin-bottom: 30px; text-align: left;">
                <strong style="color: #856404;">📌 Important Information:</strong>
                <ul style="margin: 10px 0 0 20px; color: #856404; font-size: 14px;">
                    <li>Please note your account number for future transactions</li>
                    <li>Minimum balance must be maintained at all times</li>
                    <li>SB Account: Min INR 1,000 | CA Account: Min INR 5,000</li>
                    <li>You can start transactions immediately</li>
                </ul>
            </div>
            
            <button onclick="location.href='customer-dashboard.html'" 
                    style="background-color: #4CAF50; color: white; padding: 15px 40px; font-size: 16px; border: none; border-radius: 5px; cursor: pointer; font-weight: bold; box-shadow: 0 4px 6px rgba(0,0,0,0.1); transition: all 0.3s;">
                🏠 Go Back to Dashboard
            </button>
        </div>
    `;
    
    // Insert the success page
    document.querySelector('.form-container').innerHTML = successHTML;
    
    // Add hover effect to button
    const button = document.querySelector('.form-container button');
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

// Load existing accounts on page load
loadExistingAccounts();
