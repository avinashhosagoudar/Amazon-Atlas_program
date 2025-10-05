async function handleLogin(event) {
    event.preventDefault();

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    try {
        const response = await apiCall('/customers/login', 'POST', {
            email: email,
            password: password
        });

        if (response && response.id) {
            // Save customer info
            saveToLocalStorage('customer', response);
            showMessage('message', 'Login successful! Redirecting...', 'success');
            
            setTimeout(() => {
                window.location.href = 'customer-dashboard.html';
            }, 1000);
        } else {
            showMessage('message', 'Invalid credentials!', 'error');
        }
    } catch (error) {
        showMessage('message', 'Login failed: ' + error.message, 'error');
    }
}
