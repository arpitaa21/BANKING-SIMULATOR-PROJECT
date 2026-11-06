# Project notes

The original sprint notes described several features as complete before they were present in the source. This file records the current implementation instead.

## Implemented

- Spring Boot application using Java 17, Spring MVC, Spring Data JPA, H2, and Thymeleaf.
- Dashboard for creating accounts, depositing, and withdrawing.
- REST endpoints for listing and reading accounts, creating accounts, deposits, withdrawals, and transfers.
- Account-level transaction history persisted through JPA.
- Optional SMTP low-balance email alerts.
- Append-only text activity log.
- Existing Spring application context test.

## Known limits

- H2 in-memory storage is for local demos and loses data on restart. Configure an external database for persistent hosting.
- No database `AUDIT_LOG` table or audit-log query API is implemented.
- Vercel's official function runtimes do not include Java, so the Spring Boot server needs a Java/container host.
- This simulator has no authentication, authorization, or production banking controls.
