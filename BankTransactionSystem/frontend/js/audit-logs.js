if (!requireLogin()) {
    // Redirect handled
}

const customer = getFromLocalStorage('customer');
let currentLogs = [];

// Load all audit logs for customer
async function loadAllLogs() {
    try {
        const logs = await apiCall(`/audit/user/${customer.id}`);
        currentLogs = logs;
        displayLogs(logs, 'All Audit Logs');
        updateSummary(logs);
    } catch (error) {
        showMessage('message', 'Error loading audit logs: ' + error.message, 'error');
    }
}

// Load transaction-related logs
async function loadTransactionLogs() {
    try {
        const logs = await apiCall(`/audit/user/${customer.id}/transactions`);
        currentLogs = logs;
        displayLogs(logs, 'Transaction Audit Logs');
        updateSummary(logs);
    } catch (error) {
        showMessage('message', 'Error loading transaction logs: ' + error.message, 'error');
    }
}

// Load security-related logs
async function loadSecurityLogs() {
    try {
        const logs = await apiCall(`/audit/user/${customer.id}/security`);
        currentLogs = logs;
        displayLogs(logs, 'Security Audit Logs');
        updateSummary(logs);
    } catch (error) {
        showMessage('message', 'Error loading security logs: ' + error.message, 'error');
    }
}

// Load recent logs (last 24 hours)
async function loadRecentLogs() {
    try {
        const logs = await apiCall(`/audit/user/${customer.id}/recent/24`);
        currentLogs = logs;
        displayLogs(logs, 'Recent Audit Logs (Last 24 Hours)');
        updateSummary(logs);
    } catch (error) {
        showMessage('message', 'Error loading recent logs: ' + error.message, 'error');
    }
}

// Display logs in table format
function displayLogs(logs, title) {
    document.getElementById('logsTitle').textContent = title;
    const logsTable = document.getElementById('logsTable');
    
    if (logs && logs.length > 0) {
        const table = document.createElement('table');
        table.className = 'transaction-table';
        table.innerHTML = `
            <thead>
                <tr>
                    <th>Timestamp</th>
                    <th>Action</th>
                    <th>Entity Type</th>
                    <th>Entity ID</th>
                    <th>Old Value</th>
                    <th>New Value</th>
                    <th>Details</th>
                </tr>
            </thead>
            <tbody>
                ${logs.map(log => `
                    <tr>
                        <td>${new Date(log.timestamp).toLocaleString()}</td>
                        <td><strong>${formatAction(log.action)}</strong></td>
                        <td>${log.entityType}</td>
                        <td>${log.entityId || 'N/A'}</td>
                        <td>${log.oldValue || '-'}</td>
                        <td>${log.newValue || '-'}</td>
                        <td>${log.description || '-'}</td>
                    </tr>
                `).join('')}
            </tbody>
        `;
        logsTable.innerHTML = '';
        logsTable.appendChild(table);
    } else {
        logsTable.innerHTML = '<p>No audit logs found.</p>';
    }
}

// Format action names for display
function formatAction(action) {
    const actionMap = {
        'LOGIN': '🔐 Login',
        'LOGOUT': '🚪 Logout',
        'FAILED_LOGIN': '❌ Failed Login',
        'DEPOSIT': '💵 Deposit',
        'WITHDRAW': '💸 Withdraw',
        'TRANSFER_OUT': '➡️ Transfer Out',
        'TRANSFER_IN': '⬅️ Transfer In',
        'TRANSACTION_UNDO': '↶ Undo Transaction',
        'TRANSACTION_REDO': '↷ Redo Transaction',
        'TRANSACTION_UNDOABLE': '📝 Transaction',
        'ACCOUNT_CREATION': '🏦 Account Created',
        'ACCOUNT_CLOSURE': '🔒 Account Closed',
        'PASSWORD_CHANGE': '🔑 Password Changed',
        'BALANCE_UPDATE': '💰 Balance Updated'
    };
    return actionMap[action] || action;
}

// Update summary statistics
function updateSummary(logs) {
    const summaryDiv = document.getElementById('logsSummary');
    
    const totalLogs = logs.length;
    const transactionCount = logs.filter(log => log.entityType === 'TRANSACTION').length;
    const securityCount = logs.filter(log => 
        ['LOGIN', 'LOGOUT', 'FAILED_LOGIN', 'PASSWORD_CHANGE'].includes(log.action)
    ).length;
    const accountCount = logs.filter(log => log.entityType === 'ACCOUNT').length;
    
    summaryDiv.innerHTML = `
        <h3>Audit Summary</h3>
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 15px;">
            <div style="padding: 15px; background: #f0f4ff; border-radius: 8px;">
                <p style="color: #666; margin: 0;">Total Logs</p>
                <p style="font-size: 2em; font-weight: bold; color: #667eea; margin: 5px 0 0 0;">${totalLogs}</p>
            </div>
            <div style="padding: 15px; background: #fff3cd; border-radius: 8px;">
                <p style="color: #666; margin: 0;">Transaction Logs</p>
                <p style="font-size: 2em; font-weight: bold; color: #f5a623; margin: 5px 0 0 0;">${transactionCount}</p>
            </div>
            <div style="padding: 15px; background: #ffe6e6; border-radius: 8px;">
                <p style="color: #666; margin: 0;">Security Logs</p>
                <p style="font-size: 2em; font-weight: bold; color: #f5576c; margin: 5px 0 0 0;">${securityCount}</p>
            </div>
            <div style="padding: 15px; background: #e6f7ff; border-radius: 8px;">
                <p style="color: #666; margin: 0;">Account Logs</p>
                <p style="font-size: 2em; font-weight: bold; color: #4facfe; margin: 5px 0 0 0;">${accountCount}</p>
            </div>
        </div>
    `;
}

// Load all logs by default on page load
loadAllLogs();

// Auto-refresh every 30 seconds
setInterval(() => {
    if (currentLogs.length > 0) {
        loadAllLogs();
    }
}, 30000);
