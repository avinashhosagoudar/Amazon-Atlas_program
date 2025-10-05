const API_BASE_URL = 'http://localhost:8080/api';

// Utility Functions
function showMessage(elementId, message, type) {
    const messageEl = document.getElementById(elementId);
    if (messageEl) {
        messageEl.textContent = message;
        messageEl.className = `message ${type}`;
        messageEl.style.display = 'block';
        
        // Auto hide after 5 seconds
        setTimeout(() => {
            messageEl.style.display = 'none';
        }, 5000);
    }
}

function saveToLocalStorage(key, value) {
    localStorage.setItem(key, JSON.stringify(value));
}

function getFromLocalStorage(key) {
    const data = localStorage.getItem(key);
    return data ? JSON.parse(data) : null;
}

function clearLocalStorage() {
    localStorage.clear();
}

function isLoggedIn() {
    const customer = getFromLocalStorage('customer');
    return customer !== null;
}

function requireLogin() {
    if (!isLoggedIn()) {
        window.location.href = 'login.html';
        return false;
    }
    return true;
}

function exitApp() {
    if (confirm('Are you sure you want to exit?')) {
        clearLocalStorage();
        alert('Thank you for using Banking System!');
        window.close();
    }
}

// API Call Helper
async function apiCall(endpoint, method = 'GET', body = null) {
    const options = {
        method: method,
        headers: {
            'Content-Type': 'application/json',
        },
    };

    if (body) {
        options.body = JSON.stringify(body);
    }

    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, options);
        const data = await response.json();
        
        if (!response.ok) {
            throw new Error(data.message || 'Request failed');
        }
        
        return data;
    } catch (error) {
        console.error('API Error:', error);
        throw error;
    }
}
