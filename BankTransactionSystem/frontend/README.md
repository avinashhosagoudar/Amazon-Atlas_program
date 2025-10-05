# Banking System Frontend

HTML/CSS/JavaScript frontend for the Automated Banking Transaction System.

## Features

- ✅ Customer Registration
- ✅ Customer Login
- ✅ Create Account (SB/CA)
- ✅ View Balance
- ✅ Deposit Money
- ✅ Withdraw Money
- ✅ Transfer Money
- ✅ Transaction History
- ✅ Close Account
- ✅ Change Password
- 🚧 Employee Login (Coming Soon)

## Setup

1. **Ensure Backend is Running:**
   ```bash
   cd backend
   mvn spring-boot:run
   ```
   Backend should be running on `http://localhost:8080`

2. **Serve Frontend:**
   
   **Option 1 - Python HTTP Server:**
   ```bash
   cd frontend
   python -m http.server 3000
   ```
   
   **Option 2 - Node.js HTTP Server:**
   ```bash
   cd frontend
   npx http-server -p 3000
   ```
   
   **Option 3 - VS Code Live Server:**
   - Install "Live Server" extension
   - Right-click on `index.html`
   - Select "Open with Live Server"

3. **Access Application:**
   - Open browser to: `http://localhost:3000`
   - Backend API: `http://localhost:8080/api`

## Structure

