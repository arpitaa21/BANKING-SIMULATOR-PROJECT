# 🏦 Banking Application  
### Infosys Repository

Spring Boot–based banking application demonstrating **core banking operations**, **low-balance email alerts**, and **auditing using database + file logging**.

---

## 📌 Features

### Banking Operations
- Create account  
- Deposit money  
- Withdraw money  
- Transfer funds (implemented as withdraw + deposit)

### Low-Balance Email Alerts
- Triggered when balance drops below **₹500**
- Only **one alert per low-balance event**
- Alert resets once balance ≥ ₹500

### Auditing
- Database auditing (`AUDIT_LOG` table)
- File-based logging (`transactions.txt`)

### REST APIs
- Account operations
- Audit log access

### Web UI
- Simple dashboard using **Thymeleaf**
- Basic responsive styling using **CSS**

### Database
- H2 in-memory database
- H2 Console enabled

---

## 🛠 Tech Stack

- Java 17  
- Spring Boot  
- Spring Web  
- Spring Data JPA  
- H2 Database  
- Jakarta Mail (Email alerts)  
- Thymeleaf  
- CSS  
- SLF4J + Logback (Logging)

---

## 🚀 Quick Start

### Prerequisites
- Java 17 installed  
- Maven installed and added to PATH  

### Run the Application

From the project root directory (`bankingApplication`), run:

```bash
mvn spring-boot:run


Application will start at:
    http://localhost:8080

Configuration
Edit the file:
src/main/resources/application.properties

Database Configuration
spring.datasource.url=jdbc:h2:mem:bankdb
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true


Email Configuration (Gmail SMTP)

mail.smtp.host=smtp.gmail.com
mail.smtp.port=587
mail.smtp.username=YOUR_EMAIL@gmail.com
mail.smtp.password=YOUR_APP_PASSWORD

⚠️ Note:
Use Gmail App Password, not your normal Gmail password.

🌐 API Endpoints
Accounts
➕ Create Account

POST /accounts

{
  "name": "User",
  "email": "user@example.com",
  "balance": 500
}

💰 Deposit
POST /accounts/{id}/deposit?amount=100

💸 Withdraw

POST /accounts/{id}/withdraw?amount=50

🔁 Transfer

Transfer is implemented internally as:

withdraw(fromAccount)
deposit(toAccount)


(Endpoint can be exposed later if required.)

📋 Audit Logs

GET /audit/logs
→ Fetch all audit logs

GET /audit/logs/account/{accountId}
→ Logs for a specific account

GET /audit/logs/event/{eventType}
→ Logs filtered by event type

Supported event types:

ACCOUNT_CREATED
DEPOSIT
WITHDRAW
TRANSFER
EMAIL_SENT
EMAIL_FAILED

✉ Low-Balance Email Alert
Trigger Conditions

Balance < ₹500

lowBalanceAlertSent == false

Email Details

Subject: ⚠ Low Balance Alert

Email Body Includes:

Account holder name

Current balance

Reminder to deposit funds

🗄 Viewing Data
H2 Console

Start the application

Open browser and go to:
http://localhost:8080/h2-console


Use the following details:

JDBC URL: jdbc:h2:mem:bankdb
Username: sa
Password: (leave empty)

Sample Queries
SELECT * FROM ACCOUNT;
SELECT * FROM AUDIT_LOG ORDER BY CREATED_AT DESC;

📝 File-Based Logging

File name: transactions.txt

Location: Project root directory

Contains simple text-based audit trail of transactions

📊 Logging

    Logback configuration:
        src/main/resources/logback-spring.xml

    Log Levels
        INFO – normal operations (create, deposit, withdraw, transfer, email success)

    WARN – low balance, insufficient funds
    ERROR – email failures, unexpected exceptions

🧪 Running Tests
mvn test

📂 Project Structure (Key Files)
src/main/java/com/infosys/bankingApplication/
│
├── BankingApplication.java        
│
├── controllers/
│   ├── AccountController.java     
│   ├── AuditController.java       
│   └── DashboardController.java   
│
├── services/
│   ├── AccountService.java        
│   ├── BalanceMonitorService.java 
│   ├── EmailService.java          
│   └── AuditService.java          
│
├── models/
│   ├── Account.java               
│   └── AuditLog.java             
│
├── repositories/
│   ├── AccountRepository.java
│   └── AuditLogRepository.java
│
├── loggers/
│   └── TransactionLogger.java     

src/main/resources/
│
├── application.properties
├── logback-spring.xml
├── templates/
│   ├── dashboard.html
│   └── create-account.html
│
├── static/css/
│   └── style.css
│
└── transactions.txt

###Notes

    -Gmail App Password is mandatory for email functionality
    -Email sending is synchronous
    -If email fails, EmailService throws RuntimeException
    -Low-balance alert is sent only once per event

###Conclusion

    -This project demonstrates:

    -Real-world Spring Boot banking use case

    -Email integration

    -Clean layered architecture

    -REST APIs with Web UI
