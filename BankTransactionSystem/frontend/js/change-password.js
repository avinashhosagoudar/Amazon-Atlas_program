if (!requireLogin()) {
    // Redirect handled
}

const customer = getFromLocalStorage('customer');

async function handleChangePassword(event) {
    event.preventDefault();

    const currentPassword = document.getElementById('currentPassword').value;
    const newPassword = document.getElementById('newPassword').value;
    const confirmPassword = document.getElementById('confirmPassword').value;

    if (newPassword !== confirmPassword) {
        showMessage('message', 'New passwords do not match!', 'error');
        return;
    }

    try {
        const response = await apiCall('/customers/change-password', 'PUT', {
            customerId: customer.id,
            oldPassword: currentPassword,
            newPassword: newPassword
        });

        if (response.success) {
            showMessage('message', 'Password changed successfully!', 'success');
            document.getElementById('changePasswordForm').reset();
        } else {
            showMessage('message', 'Password change failed! Please check your current password.', 'error');
        }
    } catch (error) {
        showMessage('message', 'Password change failed: ' + error.message, 'error');
    }
}
