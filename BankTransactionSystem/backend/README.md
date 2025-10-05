# Automated Banking Transaction System

A comprehensive banking system built with Java Spring Boot and MongoDB that provides secure money transfers, audit logging, and transaction management.

## Features

- Customer Registration and Authentication
- Account Management (SB/CA account types)
- Money Transfer (Deposit/Withdraw/Transfer)
- Transaction History and Audit Logs
- ACID Transaction Support
- Custom Data Structures (Stack & Queue for transactions)
- Automated Settlement Jobs
- JUnit Testing

## Technology Stack

- **Backend**: Java 17, Spring Boot 3.2
- **Database**: MongoDB (Local)
- **Testing**: JUnit 5, Mockito
- **Security**: Spring Security
- **Build**: Maven

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MongoDB 6.0+ (running locally on port 27017)

## Setup Instructions

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd banking-system
   ```

2. **Start MongoDB**
   ```bash
   mongod --dbpath /data/db
   ```

3. **Build and run the application**
   ```bash
   # Option 1: Standard build
   mvn clean install
   
   # Option 2: If you encounter dependency issues, try:
   mvn clean install -DskipTests
   
   # Option 3: If Maven cache is corrupted, clear and retry:
   mvn dependency:purge-local-repository clean install
   
   # Option 4: Use offline mode if you have dependencies cached:
   mvn clean install -o
   
   # Run the application
   mvn spring-boot:run
   ```

4. **Alternative build methods if Maven issues persist:**
   ```bash
   # Clear Maven cache completely
   rm -rf ~/.m2/repository
   mvn clean install
   
   # Or on Windows:
   rmdir /s "%USERPROFILE%\.m2\repository"
   mvn clean install
   ```

5. **Access the application**
   - Console Interface: Available on application startup
   - REST API: http://localhost:8080

## Troubleshooting

### Maven Build Issues

#### Issue 1: Maven Surefire Plugin Download Error (Tag Mismatch / SSL Error)

If you encounter errors like:
```
Could not transfer artifact org.apache.maven.surefire:surefire-shared-utils:jar:3.0.0-M9
Tag mismatch / bad_record_mac
```

**Solutions (try in order):**

1. **Skip Tests Temporarily:**
   ```bash
   mvn clean install -DskipTests
   ```

2. **Clear Maven Cache and Retry:**
   ```bash
   # Windows
   rmdir /s /q "%USERPROFILE%\.m2\repository\org\apache\maven\surefire"
   mvn clean install
   
   # Linux/Mac
   rm -rf ~/.m2/repository/org/apache/maven/surefire
   mvn clean install
   ```

3. **Use Different Maven Repository Mirror:**
   
   Create or edit `%USERPROFILE%\.m2\settings.xml` (Windows) or `~/.m2/settings.xml` (Linux/Mac):
   
   ```xml
   <settings>
     <mirrors>
       <mirror>
         <id>maven-default-http-blocker</id>
         <mirrorOf>external:http:*</mirrorOf>
         <name>Pseudo repository to mirror external repositories initially using HTTP.</name>
         <url>http://0.0.0.0/</url>
         <blocked>true</blocked>
       </mirror>
       <mirror>
         <id>google-maven-central</id>
         <name>Google Maven Central</name>
         <url>https://maven-central.storage-download.googleapis.com/maven2/</url>
         <mirrorOf>central</mirrorOf>
       </mirror>
     </mirrors>
   </settings>
   ```

4. **Downgrade Surefire Plugin Version:**
   
   Update your `pom.xml` to use a stable version:
   ```xml
   <plugin>
       <groupId>org.apache.maven.plugins</groupId>
       <artifactId>maven-surefire-plugin</artifactId>
       <version>2.22.2</version>
   </plugin>
   ```

5. **Check Network/Firewall Settings:**
   ```bash
   # Test connectivity
   curl -I https://repo.maven.apache.org/maven2/
   
   # If behind proxy, add to settings.xml
   ```

6. **Use Maven Offline Mode (if dependencies are cached):**
   ```bash
   mvn clean install -o
   ```

7. **Force Update Dependencies:**
   ```bash
   mvn clean install -U
   ```

#### Issue 2: Compilation Errors

If you encounter compilation errors:
```bash
mvn clean compile -X
```

#### Issue 3: MongoDB Connection Issues
If MongoDB connection fails:
1. Ensure MongoDB service is running
2. Check port 27017 is available
3. Verify MongoDB logs for errors

### Network connectivity issues:**
   - Check firewall settings
   - Try using VPN if corporate network blocks Maven Central
   - Use `mvn -X` for detailed debug information

## Quick Build Commands

```bash
# Standard build
mvn clean install

# Skip tests (fastest)
mvn clean install -DskipTests

# Force update all dependencies
mvn clean install -U

# Clear cache and rebuild
mvn dependency:purge-local-repository clean install

# Offline mode (use cached dependencies)
mvn clean install -o

# Debug mode
mvn clean install -X

# Specific test
mvn test -Dtest=TransactionServiceTest

# Package without running tests
mvn clean package -DskipTests

# Run application
mvn spring-boot:run
```

## Alternative Build: Without Maven Wrapper Issues

If Maven continues to have issues, try manual dependency download:

```bash
# 1. Clear everything
rmdir /s /q target
rmdir /s /q "%USERPROFILE%\.m2\repository"

# 2. Download dependencies only
mvn dependency:resolve

# 3. Build without tests
mvn clean package -DskipTests

# 4. Run directly
java -jar target/banking-system-1.0.0.jar
```

## Common Error Solutions

### Error: "bad_record_mac" or SSL/TLS Issues
- **Cause**: Network/Firewall blocking Maven Central
- **Solution**: Use alternative mirror (see above) or skip tests

### Error: "BUILD FAILURE" during test phase
- **Cause**: MongoDB not running or test dependencies missing
- **Solution**: 
  ```bash
  # Start MongoDB first
  mongod --dbpath /data/db
  
  # Then build
  mvn clean install
  ```

### Error: Spring Security blocks application
- **Cause**: Security autoconfiguration enabled
- **Solution**: Already disabled in `application.properties`

### Error: Port 8080 already in use
- **Solution**:
  ```bash
  # Windows
  netstat -ano | findstr :8080
  taskkill /PID <pid> /F
  
  # Linux/Mac
  lsof -i :8080
  kill -9 <pid>
  ```

## Performance Tips

1. **Use Maven Daemon (mvnd)** - You're already using it!
   - 2-3x faster builds
   - Keep it running for subsequent builds

2. **Parallel Builds:**
   ```bash
   mvn -T 4 clean install
   ```

3. **Skip Non-Essential Tasks:**
   ```bash
   mvn clean install -DskipTests -Dmaven.javadoc.skip=true
   ```

## Project Health Check

Before building, verify:
```bash
# Java version
java -version  # Should be 17+

# Maven version
mvn -version   # Should be 3.6+

# MongoDB status
mongod --version

# Network connectivity
ping repo.maven.apache.org
```

## Usage

### Console Menu Options:
1. Customer Register
2. Customer Login
3. Employee Login
4. Exit

### Customer Operations:
1. Create Account (SB/CA)
2. View Balance
3. Withdraw Money
4. Transfer Money
5. Transaction History
6. Close Account
7. Change Password
8. Exit

## Testing

Run tests with:
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=TransactionServiceTest

# Run tests with detailed output
mvn test -X
```

## Settlement Job

Configure cron job for automated settlement:
```bash
chmod +x scripts/settlement-job.sh
crontab -e
# Add: 0 2 * * * /path/to/banking-system/scripts/settlement-job.sh
```

## Frontend Setup

The project now includes a separate HTML/CSS/JavaScript frontend.

### Running the Frontend

1. **Start the Backend First:**
   ```bash
   mvn spring-boot:run
   ```

2. **Start the Frontend:**
   ```bash
   cd frontend
   python -m http.server 3000
   ```

3. **Access the Application:**
   - Open browser to: `http://localhost:3000`
   - Backend API: `http://localhost:8080`

### Frontend Features

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

See `frontend/README.md` for detailed frontend documentation.

## Project Structure

- `src/main/java/com/yourcompany/bankingsystem/` - Main application code
- `src/test/java/` - Unit and integration tests
- `scripts/` - DevOps scripts for settlement jobs
- `src/main/resources/` - Configuration files

## Build Without Tests

If you want to quickly build and run without tests:
```bash
mvn clean package -DskipTests
java -jar target/banking-system-1.0.0.jar
```
