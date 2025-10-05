async function handleRegister(event) {
    event.preventDefault();

    const name = document.getElementById('fullName').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;

    // Validate passwords match
    if (password !== confirmPassword) {
        showMessage('message', 'Passwords do not match!', 'error');
        return;
    }

    try {
        const response = await apiCall('/customers/register', 'POST', {
            name: name,
            email: email,
            password: password
        });

        showMessage('message', 'Customer registered successfully! Customer ID: ' + response.id, 'success');
        
        // Redirect to login after 2 seconds
        setTimeout(() => {
            window.location.href = 'login.html';
        }, 2000);
    } catch (error) {
        showMessage('message', 'Registration failed: ' + error.message, 'error');
    }
}
