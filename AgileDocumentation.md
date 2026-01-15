# 📘 Agile Execution Document

## Project: Banking Application  
**Repository:** Infosys  
**Methodology:** Agile (Sprint-based Execution)

---

## 🗓 Sprint 1 – Project Setup & Core Design  
**Duration:** 20-Nov-2025 to 30-Nov-2025

---

### 📅 20-Nov-2025
**Planned:**
- Understand problem statement  
- Define project scope  
- Decide technology stack  

**Executed:**
- Finalized core banking use cases  
- Selected Spring Boot, Java 17, H2, Thymeleaf  
- Created initial project structure  

**Owner:** Arpita Panda  
**Status:** Completed ✅

---

### 📅 25-Nov-2025
**Planned:**
- Create Account entity  
- Configure database & JPA  

**Executed:**
- Implemented `Account` entity  
- Configured H2 in-memory database  
- Verified table creation using H2 Console  

**Owner:** Arpita Panda  
**Status:** Completed ✅

---

### 📅 30-Nov-2025
**Planned:**
- Implement deposit & withdraw functionality  

**Executed:**
- Added deposit logic  
- Added withdraw logic with balance validation  
- Performed basic service-layer testing  

**Owner:** Arpita Panda  
**Status:** Completed ✅

---

## 🗓 Sprint 2 – Alerts, Auditing & Logging  
**Duration:** 05-Dec-2025 to 20-Dec-2025

---

### 📅 05-Dec-2025
**Planned:**
- Implement low-balance alert logic  

**Executed:**
- Added balance threshold check (₹500)  
- Implemented `lowBalanceAlertSent` flag to avoid duplicate alerts  

**Owner:** Arpita Panda  
**Status:** Completed ✅

---

### 📅 12-Dec-2025
**Planned:**
- Add email notification functionality  

**Executed:**
- Configured Jakarta Mail dependency  
- Implemented Gmail SMTP integration  
- Tested email alert triggering logic  

**Owner:** Arpita Panda  
**Status:** Completed ✅

---

### 📅 20-Dec-2025
**Planned:**
- Implement auditing and logging  

**Executed:**
- Created `AUDIT_LOG` database table  
- Logged all transactions into DB  
- Implemented file-based logging in `transactions.txt`  

**Owner:** Arpita Panda  
**Status:** Completed ✅

---

## 🗓 Sprint 3 – UI, APIs & Testing  
**Duration:** 05-Jan-2026 to 15-Jan-2026

---

### 📅 05-Jan-2026
**Planned:**
- Develop REST APIs  

**Executed:**
- Created APIs for account creation, deposit, withdraw, and transfer  
- Tested APIs using Postman  

**Owner:** Arpita Panda  
**Status:** Completed ✅

---

### 📅 08-Jan-2026
**Planned:**
- Develop UI using Thymeleaf  

**Executed:**
- Created dashboard page  
- Displayed account details and audit logs  
- Applied responsive CSS styling  

**Owner:** Arpita Panda  
**Status:** Completed ✅

---

### 📅 10-Jan-2026
**Planned:**
- Testing & documentation  

**Executed:**
- Wrote unit tests  
- Created `README.md` documentation  
- Final code cleanup and optimization  

**Owner:** Arpita Panda  
**Status:** Completed ✅

---

## ✅ Final Outcome

- Core banking operations successfully implemented  
- Low-balance email alert system working correctly  
- Complete auditing using database and file logging  
- REST APIs and Web UI fully functional  
- Project ready for **demo, evaluation, and interviews**
