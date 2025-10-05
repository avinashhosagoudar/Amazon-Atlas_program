#!/bin/bash

# Banking System Settlement Job Script
# This script is designed to run daily via cron for automated settlement

# Configuration
PROJECT_DIR="/path/to/banking-system"
JAR_FILE="$PROJECT_DIR/target/banking-system-1.0.0.jar"
LOG_DIR="$PROJECT_DIR/logs"
SETTLEMENT_LOG="$LOG_DIR/settlement.log"
JAVA_OPTS="-Xmx512m -Xms256m"

# Create logs directory if it doesn't exist
mkdir -p "$LOG_DIR"

# Function to log messages
log_message() {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] $1" >> "$SETTLEMENT_LOG"
}

# Function to check if MongoDB is running
check_mongodb() {
    if ! pgrep -x "mongod" > /dev/null; then
        log_message "ERROR: MongoDB is not running"
        return 1
    fi
    return 0
}

# Function to check if application JAR exists
check_jar_file() {
    if [ ! -f "$JAR_FILE" ]; then
        log_message "ERROR: JAR file not found at $JAR_FILE"
        return 1
    fi
    return 0
}

# Function to run settlement
run_settlement() {
    log_message "INFO: Starting daily settlement process"
    
    # Run the settlement command using Spring Boot's actuator endpoint
    SETTLEMENT_RESULT=$(curl -s -X POST http://localhost:8080/api/settlement/daily 2>&1)
    
    if [ $? -eq 0 ]; then
        log_message "INFO: Settlement completed successfully"
        log_message "INFO: Settlement result: $SETTLEMENT_RESULT"
        return 0
    else
        log_message "ERROR: Settlement failed with error: $SETTLEMENT_RESULT"
        return 1
    fi
}

# Function to send notification (email/slack etc.)
send_notification() {
    local status="$1"
    local message="$2"
    
    # Example: Send email notification (requires mail command to be configured)
    # echo "$message" | mail -s "Banking System Settlement - $status" admin@yourcompany.com
    
    # Example: Send Slack notification (requires webhook URL)
    # curl -X POST -H 'Content-type: application/json' \
    #     --data "{\"text\":\"Banking System Settlement - $status: $message\"}" \
    #     YOUR_SLACK_WEBHOOK_URL
    
    log_message "INFO: Notification sent - $status: $message"
}

# Function to cleanup old logs
cleanup_old_logs() {
    log_message "INFO: Cleaning up old log files"
    
    # Remove log files older than 30 days
    find "$LOG_DIR" -name "*.log" -type f -mtime +30 -delete 2>/dev/null
    
    # Remove old settlement reports
    find "$LOG_DIR" -name "settlement-*.txt" -type f -mtime +90 -delete 2>/dev/null
    
    log_message "INFO: Log cleanup completed"
}

# Function to generate settlement report
generate_report() {
    local report_file="$LOG_DIR/settlement-$(date '+%Y-%m-%d').txt"
    
    log_message "INFO: Generating settlement report: $report_file"
    
    cat << EOF > "$report_file"
Banking System - Daily Settlement Report
Date: $(date '+%Y-%m-%d %H:%M:%S')
========================================

Settlement Status: SUCCESS
MongoDB Status: RUNNING
Application Status: RUNNING

Transaction Summary:
- Total Processed: $(curl -s http://localhost:8080/api/transactions/count/today || echo "N/A")
- Success Rate: $(curl -s http://localhost:8080/api/transactions/success-rate/today || echo "N/A")%
- Failed Transactions: $(curl -s http://localhost:8080/api/transactions/failed/today || echo "N/A")

System Health:
- CPU Usage: $(top -bn1 | grep "Cpu(s)" | awk '{print $2}' | cut -d'%' -f1 || echo "N/A")%
- Memory Usage: $(free | grep Mem | awk '{printf("%.2f%%"), $3/$2 * 100.0}' || echo "N/A")
- Disk Usage: $(df -h $PROJECT_DIR | awk 'NR==2{print $5}' || echo "N/A")

Log Files:
- Application Log: $(wc -l < "$LOG_DIR/banking-system.log" || echo "0") lines
- Transaction Log: $(wc -l < "$LOG_DIR/transactions.log" || echo "0") lines
- Audit Log: $(wc -l < "$LOG_DIR/audit.log" || echo "0") lines

Report Generated: $(date '+%Y-%m-%d %H:%M:%S')
EOF
    
    log_message "INFO: Settlement report generated successfully"
}

# Main execution
main() {
    log_message "INFO: Settlement job started"
    
    # Pre-flight checks
    if ! check_mongodb; then
        send_notification "FAILED" "MongoDB is not running"
        exit 1
    fi
    
    if ! check_jar_file; then
        send_notification "FAILED" "Application JAR file not found"
        exit 1
    fi
    
    # Check if application is running
    if ! curl -s http://localhost:8080/actuator/health > /dev/null; then
        log_message "WARNING: Application not responding, attempting to start"
        
        # Start the application (adjust as needed for your deployment)
        cd "$PROJECT_DIR"
        nohup java $JAVA_OPTS -jar "$JAR_FILE" > "$LOG_DIR/app.out" 2>&1 &
        
        # Wait for application to start
        sleep 30
        
        if ! curl -s http://localhost:8080/actuator/health > /dev/null; then
            log_message "ERROR: Failed to start application"
            send_notification "FAILED" "Could not start banking application"
            exit 1
        fi
    fi
    
    # Run settlement process
    if run_settlement; then
        generate_report
        cleanup_old_logs
        send_notification "SUCCESS" "Daily settlement completed successfully"
        log_message "INFO: Settlement job completed successfully"
        exit 0
    else
        send_notification "FAILED" "Settlement process failed - check logs"
        log_message "ERROR: Settlement job failed"
        exit 1
    fi
}

# Execute main function
main "$@"
