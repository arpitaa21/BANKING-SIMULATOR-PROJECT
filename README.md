# Banking Simulator

A Spring Boot banking simulator with a Thymeleaf dashboard, REST API, per-account transaction history, file activity logging, and optional low-balance email alerts.

## Requirements

- Java 17
- No separate Maven installation is needed; the Maven wrapper is included.

## Run locally

From the repository root:

```powershell
./mvnw.cmd spring-boot:run
```

Open <http://localhost:8080/> for the dashboard. The app uses an in-memory H2 database by default, so account data is cleared when it stops. To inspect local data in the H2 console, set `SPRING_H2_CONSOLE_ENABLED=true` before starting it, then open <http://localhost:8080/h2-console> (JDBC URL `jdbc:h2:mem:bankdb`, user `sa`, blank password).

Run the existing tests with `./mvnw.cmd test`.

## Configuration

The defaults are suitable for a local demo. Set these environment variables when needed:

| Variable | Purpose |
| --- | --- |
| `SPRING_DATASOURCE_URL` | JDBC URL for a persistent database |
| `SPRING_DATASOURCE_USERNAME` | Database user |
| `SPRING_DATASOURCE_PASSWORD` | Database password |
| `MAIL_SMTP_HOST` | SMTP server (defaults to Gmail) |
| `MAIL_SMTP_PORT` | SMTP port (defaults to 587) |
| `MAIL_SMTP_USERNAME` | SMTP account; blank disables email |
| `MAIL_SMTP_PASSWORD` | SMTP app password |
| `BANKING_TRANSACTION_LOG_PATH` | Activity log file path (defaults to `transactions.txt`) |
| `SPRING_H2_CONSOLE_ENABLED` | Enable the local H2 console (defaults to `false`) |
| `PORT` | HTTP port supplied by a container host (defaults to `8080`) |
| `FRONTEND_ORIGINS` | Comma-separated origins allowed to call the API from a browser |

Do not commit real credentials. If SMTP is not configured, the app continues to work and skips sending email. SMTP failures are logged and do not undo a completed bank operation.

## Features and rules

- Account balances start at zero or more.
- Currency uses decimal values with a maximum of two fractional digits.
- Deposits and withdrawals must be positive, and a withdrawal cannot exceed the current balance.
- Balances are capped at ₹1,000,000.
- A low-balance email alert is attempted once after a balance drops below ₹500; the flag resets when the balance returns to ₹500 or more.
- Deposit, withdrawal, and transfer operations keep transaction history on each account. Transfers run in one database transaction.

## API

- `GET /accounts` — list accounts
- `GET /accounts/{id}` — get one account
- `POST /accounts` — create an account with JSON such as `{"name":"User","email":"user@example.com","balance":500}`
- `POST /accounts/{id}/deposit?amount=100`
- `POST /accounts/{id}/withdraw?amount=50`
- `POST /accounts/transfer?fromId=1&toId=2&amount=100`

Invalid requests return an appropriate 4xx response with a Problem Details body. The web dashboard is at `/dashboard`; `/health` is available for container-host health checks.

## Deploy the backend and frontend separately

The root `Dockerfile` builds the Spring Boot API for a Java/container host. Configure an external PostgreSQL database for durable data using `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, and `SPRING_DATASOURCE_PASSWORD`; the local H2 default is in-memory.

The static UI in `frontend/` can be deployed on Vercel with project root directory `frontend` and the **Other** framework preset. It has no build step. After deploying the backend, enter its public URL in the frontend's **Backend address** field and set `FRONTEND_ORIGINS` on the backend to the Vercel site origin(s). Vercel's [official Functions runtimes](https://vercel.com/docs/functions/runtimes) do not include Java, so the Spring Boot API cannot run there as-is. No Vercel project connected in this session matches this app.

For local split-frontend development, start the backend and run `python -m http.server 3000 --directory frontend`; then visit <http://localhost:3000>. The default backend CORS origin permits this local UI.

This is an unauthenticated demo using in-memory storage by default. Do not use it for real financial data or expose it publicly without adding authentication, authorization, durable storage, and operational controls.
