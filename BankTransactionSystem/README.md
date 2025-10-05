# 🏦 Automated Banking Transaction System

A full-stack banking application built with **Spring Boot** (Backend) and **HTML/CSS/JavaScript** (Frontend) that provides secure banking operations with advanced features like transaction undo/redo and comprehensive audit logging.

---

## 📋 Table of Contents

- [Features](#-features)
- [Technology Stack](#-technology-stack)
- [Project Structure](#-project-structure)
- [Prerequisites](#-prerequisites)
- [Installation & Setup](#-installation--setup)
- [Running the Application](#-running-the-application)
- [API Endpoints](#-api-endpoints)
- [Database Schema](#-database-schema)
- [Account Types & Limits](#-account-types--limits)
- [Custom Data Structures](#-custom-data-structures)
- [Design Patterns](#-design-patterns)
- [Testing](#-testing)


---

## ✨ Features

### Core Banking Operations
- ✅ **Customer Management** - Register, login, change password
- ✅ **Account Management** - Create SB/CA accounts, view balance, close accounts
- ✅ **Transactions** - Deposit, withdraw, transfer with real-time validation
- ✅ **Transaction History** - Complete audit trail with search and filters
- ✅ **Undo/Redo Functionality** - Reverse transactions using Command Pattern
- ✅ **Audit Logging** - Comprehensive security and compliance tracking

### Advanced Features
- 🔒 **Secure Authentication** - Password-based verification for sensitive operations
- 💰 **Transaction Limits** - Per-transaction and daily limits enforcement
- 📊 **Real-time Balance Updates** - Instant balance reflection across all operations
- 🔄 **Automatic Settlement** - Daily/monthly transaction settlement jobs
- 📈 **Transaction Analytics** - Statistics and reporting capabilities
- 🎯 **ACID Compliance** - Transaction integrity with MongoDB transactions

---

## 🛠️ Technology Stack

### Backend
- **Java 17** - Programming language
- **Spring Boot 3.2.0** - Application framework
- **Spring Data MongoDB** - Database integration
- **Spring Security** - Authentication & authorization
- **Maven** - Build tool & dependency management

### Frontend
- **HTML5** - Structure
- **CSS3** - Styling with gradients and animations
- **JavaScript (ES6+)** - Client-side logic
- **Fetch API** - REST API communication

### Database
- **MongoDB 6.0+** - NoSQL database for flexible schema

### Testing
- **JUnit 5** - Unit testing framework
- **Mockito** - Mocking framework for tests

---

## 📁 Project Structure

```
banking-system/
│
├── backend/                          # Spring Boot Backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/yourcompany/bankingsystem/
│   │   │   │   ├── model/           # Entity classes
│   │   │   │   │   ├── Customer.java
│   │   │   │   │   ├── Account.java
│   │   │   │   │   ├── Transaction.java
│   │   │   │   │   ├── AuditLog.java
│   │   │   │   │   └── custom/      # Custom data structures
│   │   │   │   │       ├── TransactionQueue.java
│   │   │   │   │       ├── TransactionStack.java
│   │   │   │   │       └── TransactionHistory.java
│   │   │   │   │
│   │   │   │   ├── repository/      # MongoDB repositories
│   │   │   │   │   ├── CustomerRepository.java
│   │   │   │   │   ├── AccountRepository.java
│   │   │   │   │   ├── TransactionRepository.java
│   │   │   │   │   └── AuditLogRepository.java
│   │   │   │   │
│   │   │   │   ├── service/         # Business logic
│   │   │   │   │   ├── CustomerService.java
│   │   │   │   │   ├── AccountService.java
│   │   │   │   │   ├── TransactionService.java
│   │   │   │   │   ├── AuditLogService.java
│   │   │   │   │   └── SettlementService.java
│   │   │   │   │
│   │   │   │   ├── controller/      # REST API endpoints
│   │   │   │   │   ├── CustomerController.java
│   │   │   │   │   ├── AccountController.java
│   │   │   │   │   ├── TransactionController.java
│   │   │   │   │   ├── TransactionHistoryController.java
│   │   │   │   │   ├── AuditLogController.java
│   │   │   │   │   └── SettlementController.java
│   │   │   │   │
│   │   │   │   ├── config/          # Configuration classes
│   │   │   │   │   ├── MongoConfig.java
│   │   │   │   │   ├── SecurityConfig.java
│   │   │   │   │   └── WebConfig.java
│   │   │   │   │
│   │   │   │   ├── util/            # Utility classes
│   │   │   │   │   └── LoggingUtil.java
│   │   │   │   │
│   │   │   │   └── BankingSystemApplication.java  # Main application
│   │   │   │
│   │   │   └── resources/
│   │   │       ├── application.properties  # App configuration
│   │   │       └── logback.xml            # Logging configuration
│   │   │
│   │   └── test/                    # Unit & integration tests
│   │       └── java/com/yourcompany/bankingsystem/
│   │           ├── service/
│   │           │   ├── TransactionServiceTest.java
│   │           │   └── TransactionHistoryTest.java
│   │           └── controller/
│   │               └── TransactionControllerTest.java
│   │
│   ├── pom.xml                      # Maven dependencies
│   └── README.md                    # Backend documentation
│
├── frontend/                         # HTML/CSS/JS Frontend
│   ├── pages/                       # HTML pages
│   │   ├── login.html
│   │   ├── register.html
│   │   ├── customer-dashboard.html
│   │   ├── create-account.html
│   │   ├── view-balance.html
│   │   ├── deposit.html
│   │   ├── withdraw.html
│   │   ├── transfer.html
│   │   ├── transaction-history.html
│   │   ├── close-account.html
│   │   ├── change-password.html
│   │   ├── undo-redo.html
│   │   ├── audit-logs.html
│   │   └── employee-login.html
│   │
│   ├── js/                          # JavaScript files
│   │   ├── main.js                 # Shared utilities
│   │   ├── login.js
│   │   ├── register.js
│   │   ├── dashboard.js
│   │   ├── account.js
│   │   ├── balance.js
│   │   ├── deposit.js
│   │   ├── withdraw.js
│   │   ├── transfer.js
│   │   ├── history.js
│   │   ├── close-account.js
│   │   ├── change-password.js
│   │   ├── undo-redo.js
│   │   └── audit-logs.js
│   │
│   ├── css/
│   │   └── style.css               # Global styles
│   │
│   ├── index.html                   # Landing page
│   └── README.md                    # Frontend documentation
│
├── docs/                            # Documentation
│   └── diagrams/                    # UML & ER diagrams
│       ├── uml-class-diagram.puml
│       ├── er-diagram.puml
│       ├── system-architecture.puml
│       ├── sequence-deposit.puml
│       ├── sequence-transfer.puml
│       ├── sequence-undo.puml
│       ├── sequence-redo.puml
│       └── README.md
│
└── README.md                        # This file
```

---

## 📦 Prerequisites

Before running the application, ensure you have:

- ☑️ **Java 17 or higher** - [Download](https://adoptium.net/)
- ☑️ **Maven 3.6+** - [Download](https://maven.apache.org/download.cgi)
- ☑️ **MongoDB 6.0+** - [Download](https://www.mongodb.com/try/download/community)
- ☑️ **Git** - [Download](https://git-scm.com/downloads)
- ☑️ **Web Browser** (Chrome, Firefox, Edge)

**Verify installations:**
```bash
java -version    # Should show Java 17+
mvn -version     # Should show Maven 3.6+
mongod --version # Should show MongoDB 6.0+
```

---

## 🚀 Installation & Setup

### Step 1: Clone the Repository
```bash
git clone <repository-url>
cd banking-system
```

### Step 2: Start MongoDB
```bash
# Windows
mongod --dbpath C:\data\db

# Linux/Mac
mongod --dbpath /data/db
```

MongoDB should run on `localhost:27017` (default port).

### Step 3: Configure Backend (Optional)
Edit `backend/src/main/resources/application.properties` if needed:

```properties
# MongoDB Configuration
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=banking_system

# Server Configuration
server.port=8080
```

### Step 4: Build Backend
```bash
cd backend

# Clean build
mvn clean install

# Skip tests (faster build)
mvn clean install -DskipTests
```

---

## 🎯 Running the Application

### Option 1: Run Backend & Frontend Separately (Recommended)

**Terminal 1 - Backend:**
```bash
cd backend
mvn spring-boot:run
```
Backend runs on: `http://localhost:8080`

**Terminal 2 - Frontend:**
```bash
cd frontend

# Using Python
python -m http.server 3000

# OR using Node.js
npx http-server -p 3000
```
Frontend runs on: `http://localhost:3000`

### Option 2: Console Application Only
```bash
cd backend
mvn spring-boot:run
```
Use the console menu for banking operations.

### Option 3: Using JAR File
```bash
cd backend
mvn clean package -DskipTests
java -jar target/banking-system-1.0.0.jar
```

---

## 🌐 API Endpoints

### Customer Management

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| `POST` | `/api/customers/register` | Register new customer | `{name, email, password}` |
| `POST` | `/api/customers/login` | Customer login | `{email, password}` |
| `PUT` | `/api/customers/change-password` | Change password | `{customerId, oldPassword, newPassword}` |

**Example:**
```bash
curl -X POST http://localhost:8080/api/customers/register \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","email":"john@example.com","password":"pass123"}'
```

---

### Account Management

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| `POST` | `/api/accounts/create` | Create new account | `{customerId, accountType, initialDeposit}` |
| `GET` | `/api/accounts/customer/{customerId}` | Get all accounts for customer | - |
| `GET` | `/api/accounts/{accountNumber}` | Get account by number | - |
| `POST` | `/api/accounts/close` | Close account | `{accountNumber, customerId, password}` |
| `GET` | `/api/accounts/balance/{customerId}` | Get total balance | - |

**Example:**
```bash
curl -X POST http://localhost:8080/api/accounts/create \
  -H "Content-Type: application/json" \
  -d '{"customerId":"123","accountType":"SB","initialDeposit":10000}'
```

---

### Transaction Operations

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| `POST` | `/api/transactions/deposit` | Deposit money | `{accountNumber, amount}` |
| `POST` | `/api/transactions/withdraw` | Withdraw money | `{accountNumber, amount, password}` |
| `POST` | `/api/transactions/transfer` | Transfer money | `{fromAccountNumber, toAccountNumber, amount, password}` |
| `GET` | `/api/transactions/history/{accountNumber}` | Get transaction history | - |
| `GET` | `/api/transactions/recent` | Get recent transactions | - |
| `GET` | `/api/transactions/pending` | Get pending transactions | - |
| `POST` | `/api/transactions/process-pending` | Process pending queue | - |
| `GET` | `/api/transactions/limits/{accountType}` | Get transaction limits | - |
| `GET` | `/api/transactions/statistics?startDate={start}&endDate={end}` | Get statistics | - |

**Example:**
```bash
curl -X POST http://localhost:8080/api/transactions/deposit \
  -H "Content-Type: application/json" \
  -d '{"accountNumber":"1234567890123","amount":5000}'
```

---

### Transaction History (Undo/Redo)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/transaction-history/undo` | Undo last transaction |
| `POST` | `/api/transaction-history/undo/{accountNumber}` | Undo last transaction for account |
| `POST` | `/api/transaction-history/redo` | Redo last undone transaction |
| `POST` | `/api/transaction-history/redo/{accountNumber}` | Redo for specific account |
| `GET` | `/api/transaction-history/can-undo` | Check if undo available |
| `GET` | `/api/transaction-history/can-redo` | Check if redo available |
| `GET` | `/api/transaction-history/undoable` | Get undoable transactions |
| `GET` | `/api/transaction-history/undoable/{accountNumber}` | Get undoable for account |
| `GET` | `/api/transaction-history/redoable` | Get redoable transactions |
| `GET` | `/api/transaction-history/redoable/{accountNumber}` | Get redoable for account |
| `GET` | `/api/transaction-history/summary` | Get history summary |

**Example:**
```bash
curl -X POST http://localhost:8080/api/transaction-history/undo
```

---

### Audit Logging

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/audit/user/{userId}` | Get audit logs by user |
| `GET` | `/api/audit/action/{action}` | Get logs by action type |
| `GET` | `/api/audit/security` | Get security-related logs |
| `GET` | `/api/audit/transactions` | Get transaction logs |
| `GET` | `/api/audit/recent/{hours}` | Get recent logs |

---

### Settlement Operations

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/settlement/daily` | Perform daily settlement |
| `POST` | `/api/settlement/monthly` | Perform monthly settlement |
| `POST` | `/api/settlement/reconcile` | Reconcile pending transactions |
| `GET` | `/api/settlement/transactions?startDate={start}&endDate={end}` | Get settlement transactions |

---

## 🗄️ Database Schema

### Collections

#### 1. **customers**
```javascript
{
  _id: ObjectId,
  name: String,
  email: String (unique),
  password: String,
  active: Boolean,
  createdDate: DateTime,
  lastModifiedDate: DateTime
}
```

#### 2. **accounts**
```javascript
{
  _id: ObjectId,
  accountNumber: String (unique),
  customerId: String (FK),
  accountType: String ("SB" | "CA"),
  balance: Double,
  active: Boolean,
  createdDate: DateTime,
  lastModifiedDate: DateTime
}
```

#### 3. **transactions**
```javascript
{
  _id: ObjectId,
  transactionId: String (unique),
  fromAccountNumber: String,
  toAccountNumber: String,
  amount: Double,
  transactionType: String ("DEPOSIT" | "WITHDRAW" | "TRANSFER"),
  status: String ("SUCCESS" | "FAILED" | "PENDING"),
  description: String,
  balanceAfter: Double,
  transactionDate: DateTime
}
```

#### 4. **audit_logs**
```javascript
{
  _id: ObjectId,
  userId: String,
  action: String,
  entityType: String,
  entityId: String,
  oldValue: String,
  newValue: String,
  ipAddress: String,
  userAgent: String,
  timestamp: DateTime
}
```

---

## 💳 Account Types & Limits

### Savings Bank (SB) Account

| Property | Value |
|----------|-------|
| **Minimum Balance** | INR 1,000 |
| **Initial Deposit** | INR 1,000+ |
| **Min Transaction** | INR 0.01 |
| **Max Transaction** | INR 50,000 |
| **Daily Limit** | INR 1,00,000 |
| **Accounts per Customer** | 1 (One) |

### Current Account (CA)

| Property | Value |
|----------|-------|
| **Minimum Balance** | INR 5,000 |
| **Initial Deposit** | INR 5,000+ |
| **Min Transaction** | INR 500 |
| **Max Transaction** | INR 2,00,000 |
| **Daily Limit** | INR 5,00,000 |
| **Accounts per Customer** | 1 (One) |

**Note:** Daily limit applies to combined WITHDRAW + TRANSFER operations.

---

## 📊 Custom Data Structures

### 1. TransactionQueue (FIFO)
- **Purpose:** Pending transactions processing
- **Operations:** Enqueue, Dequeue, Peek
- **Max Size:** 1,000 transactions
- **Use Case:** Batch processing, async operations

### 2. TransactionStack (LIFO)
- **Purpose:** Recent transactions tracking
- **Operations:** Push, Pop, Peek
- **Max Size:** 1,000 transactions
- **Use Case:** Quick access to recent activity

### 3. TransactionHistory (Dual Stack)
- **Purpose:** Undo/Redo functionality
- **Structure:** Undo Stack + Redo Stack
- **Max Size:** 100 undoable transactions
- **Pattern:** Command Pattern implementation

---

## 🎨 Design Patterns

| Pattern | Implementation | Purpose |
|---------|---------------|---------|
| **MVC** | Controllers, Services, Repositories | Separation of concerns |
| **Repository** | Spring Data MongoDB | Data access abstraction |
| **Service Layer** | Business logic classes | Transaction management |
| **Command** | TransactionHistory | Undo/Redo operations |
| **Dependency Injection** | Spring @Autowired | Loose coupling |
| **DTO** | Inner classes | Clean API responses |

---

## 🧪 Testing

### Run All Tests
```bash
cd backend
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=TransactionServiceTest
```

### Run with Coverage
```bash
mvn clean test jacoco:report
```

### Test Files
- `TransactionServiceTest.java` - Core transaction logic
- `TransactionHistoryTest.java` - Undo/Redo functionality
- `TransactionControllerTest.java` - REST API endpoints

---

## 🔧 Troubleshooting

### Issue: MongoDB Connection Failed
**Solution:**
```bash
# Ensure MongoDB is running
mongod --dbpath /data/db

# Check connection
mongo --eval "db.version()"
```

### Issue: Port 8080 Already in Use
**Solution:**
```bash
# Find process using port 8080
netstat -ano | findstr :8080  # Windows
lsof -i :8080                 # Linux/Mac

# Kill the process
taskkill /PID <pid> /F        # Windows
kill -9 <pid>                 # Linux/Mac
```

### Issue: Maven Build Failure
**Solution:**
```bash
# Clear Maven cache
rm -rf ~/.m2/repository  # Linux/Mac
rmdir /s "%USERPROFILE%\.m2\repository"  # Windows

# Rebuild
mvn clean install -U
```

### Issue: Frontend Not Loading Latest Changes
**Solution:**
```bash
# Hard refresh browser
Ctrl + F5  # Windows
Cmd + Shift + R  # Mac

# Clear browser cache
Ctrl + Shift + Delete
```

---

## 📝 Configuration Files

### application.properties
```properties
# Server
server.port=8080

# MongoDB
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=banking_system

# Logging
logging.level.com.yourcompany.bankingsystem=DEBUG

# Custom Banking
banking.minimum.balance.sb=1000.0
banking.minimum.balance.ca=5000.0
banking.transaction.queue.size=1000
banking.transaction.stack.size=1000
```

### logback.xml
Logs are stored in:
- `logs/banking-system.log` - Application logs
- `logs/transactions.log` - Transaction logs
- `logs/audit.log` - Audit logs
- `logs/error.log` - Error logs

---

## 🚀 Deployment

### Production Build
```bash
cd backend
mvn clean package -DskipTests
java -jar target/banking-system-1.0.0.jar
```

### Environment Variables
```bash
# Set MongoDB credentials
export MONGO_HOST=production-host
export MONGO_PORT=27017
export MONGO_DB=banking_system
export MONGO_USER=admin
export MONGO_PASS=secure_password
```

---

## 📚 Additional Resources

- **UML Diagrams:** See `docs/diagrams/README.md`
- **Backend Docs:** See `backend/README.md`
- **Frontend Docs:** See `frontend/README.md`
- **API Testing:** Use Postman collection (export available)
- **Database Scripts:** MongoDB init scripts in `scripts/`

---


**Happy Banking! 🏦💰**