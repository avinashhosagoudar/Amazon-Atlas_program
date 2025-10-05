build this project very systamatically and follow bellow instrction properly don't miss anything make very consiusly
'''
Banking Transaction System
Requirements
- Money transfer, audit logs.
Design
- Customer, Account, Transaction diagrams.
 Java
- Deposit/withdraw services.
Mongo DB(local coonect)
- ACID transaction queries.
Logs
- Insert audit logs.
Mongo DB (local coonect)
- Eventual logs in NoSQL.
Day 7 – DS
- Stack &amp; queue for transactions.
– J unit
- J unit  test for transfers.
 DevOps

- Linux cron jobs for settlement.
'''

Problem Statement : Automated Banking System

Background: As technology continues to advance, the banking sector is striving to
enhance its services by integrating automation and digitalization. The goal is to provide
customers with a seamless and efficient banking experience. This project aims to
develop an Automated Banking System that leverages Java Full Stack technologies to
streamline various banking operations.
Problem Description:
Traditional banking processes involve manual tasks that can be time-consuming and error
prone. The proposed Automated Banking System aims to address these challenges by
automating key banking operations, ensuring accuracy, speed, and improved customer
satisfaction.

User stories

1- User Story: Create a customer
Given the application is working with without any compile errors
And the user is on the main menu with options
1. Customer Register
2. Customer login
3. Employee Login
4. Exit
When the user chooses 1
Then the system prompts for customer details as

enter your full Name : AAAAAA KKKKK
Enter your email: AAAAAA @AAAAAA .com
choose Your password: ********
choose Your password again : ********

And the user provides valid account information
And the system creates a new customer with the provided details
And thank you message is shown

2- User Story: Customer login
Given the application is working with without any compile errors
And the user is on the main menu with options
1. Customer Register
2. Customer login
3. Employee Login
4. Exit

When the option selected a 2 (customer login)
Then the system shall take
Email, password
When the details are valid
Then the system shall show
1. Create Account
2. View Balance
3. Withdraw Money
4. Transfer Money
5. Transaction History
6. Close Account
7. Change Password
8. Exit

3- User Story: create account

Given the customer is successfully logged in
And the option selected is Create Account
Then system shall prompt for
Enter type of the account : ______ (should be only : SB/CA)
Enter initial Deposit : ________ (check for only +ve integer)
When the details entered are valid

Then the account to be created
And successful message to be shown

4- User Story : View Balance

Given the customer is successfully logged in
And the option selected is View Balance
Then the application shall show all the accounts associated with the customer
Like
(Ac No : 12345-Type:SB, Ac No : 1442-Type:CA)

When selected the account number
Then the application shall show the balance INR : xxxxx

5- User Story: Withdraw money from Account

Given the customer is successfully logged in
And the option selected is Withdraw money
Then the list of accounts associated with the customer to be shown
(Ac No : 12345-Balance:10,000, Ac No : 1442-Balance: )

When the customer enter the account number from which the withdraw to happen
Then the application to ask for the amount to be withdrawn
When the amount entered is less than the amount in the balance
Then the application shall show message “Withdraw Successful“
And the application shall show remaining balance

6- User Story: Withdraw money from Account (Fail Case)

Given the customer is successfully logged in
And the option selected is Withdraw money
Then the list of accounts associated with the customer to be shown
(Ac No : 12345-Balance:10,000, Ac No : 1442-Balance: )

When the customer enter the account number from which the withdraw to happen
Then the application to ask for the amount to be withdrawn
When the amount entered is more than the amount in the balance
Then the application shall show message “Sorry amount entered is invalid/more than
your balance“
And the application shall show balance

7- User Story: Transfer Money (Intra Bank Only)

Given the customer is successfully logged in
And the option selected is Transfer Money
Then the application shall ask from which account the money to be transferred, please
show the list of accounts associated with customer

(Ac No : 12345-Balance:10,000, Ac No : 1442-Balance: )
When the user selects the account number from the shown list
Then the application shall show a message you amount will be debited from : xxxxx
And ask for the To account number
When entered the account number validate in the DB / persistent storage for the
validity
Then ask the user amount to be transferred
Given the amount entered is less than the current balance
When the transfer is successful

Then show an message “Amount Transferred” successfully
-----------------------------------------------------------------
i want this file strcture and follow these
banking-system/
│
├── pom.xml
├── README.md
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── yourcompany/
│   │   │           └── bankingsystem/
│   │   │               ├── BankingSystemApplication.java      # Spring Boot main class
│   │   │
│   │   │               ├── config/                           # Configurations (MongoDB, DynamoDB, etc.)
│   │   │
│   │   │               ├── controller/
│   │   │               │   ├── CustomerController.java
│   │   │               │   ├── AccountController.java
│   │   │               │   ├── TransactionController.java
│   │   │               │   ├── AuditLogController.java
│   │   │
│   │   │               ├── model/
│   │   │               │   ├── Customer.java
│   │   │               │   ├── Account.java
│   │   │               │   ├── Transaction.java
│   │   │               │   ├── AuditLog.java
│   │   │               │   ├── custom/
│   │   │               │       ├── TransactionStack.java     # Custom Stack for transactions
│   │   │               │       └── TransactionQueue.java     # Custom Queue for transactions
│   │   │
│   │   │               ├── repository/
│   │   │               │   ├── CustomerRepository.java       # MongoDB
│   │   │               │   ├── AccountRepository.java        # MongoDB
│   │   │               │   ├── TransactionRepository.java    # MongoDB
│   │   │               │   ├── AuditLogRepository.java       # MongoDB & DynamoDB service adapters
│   │   │
│   │   │               ├── service/
│   │   │               │   ├── CustomerService.java
│   │   │               │   ├── AccountService.java
│   │   │               │   ├── TransactionService.java
│   │   │               │   ├── AuditLogService.java
│   │   │               │   ├── SettlementService.java        # For batch/cron settlement logic
│   │   │
│   │   │               └── util/
│   │   │                   ├── MongoConfig.java
│   │   │                   ├─
│   │   │                   └── LoggingUtil.java
│   │   │
│   │   └── resources/
│   │       ├── application.properties
│   │       └── logback.xml
│   │
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── yourcompany/
│       │           └── bankingsystem/
│       │               ├── service/
│       │               │   └── TransactionServiceTest.java   # Example JUnit test
│       │               └── controller/
│       │                   └── TransactionControllerTest.java
│       │
│       └── resources/
│           └── test-data.json
│
├── scripts/
│   └── settlement-job.sh          # Shell script for cron job
│

└── .gitignore