# Quarkus BFF + Microservices POC

This repo is a **proof-of-concept** to understand:
- **Quarkus** project structure (`resource/`, `client/`, config)
- **Microservices** split by domain
- **BFF (Backend-for-Frontend)** that aggregates multiple services into a single API
- **JWT auth** protecting endpoints

## Modules

- **BFF**: `backend/bff` (port **8080**)
- **account-service**: `services/account-service` (port **8081**)
- **transaction-service**: `services/transaction-service` (port **8082**)
- **budget-service**: `services/budget-service` (port **8083**)
- **frontend**: `frontend` (Vite + TypeScript scaffold)

## Prerequisites

- **JDK 21**
- **Node.js** (for `frontend/`)
- Maven is **not required** (repo includes `mvnw.cmd`)

## Run the backend (dev mode)

Start each service in its own PowerShell terminal.

### 1) account-service (8081)

```powershell
cd "C:\Benchmark Final Projects\PersonalProject\QurkusProject\services\account-service"
.\mvnw.cmd quarkus:dev
```

### 2) transaction-service (8082)

```powershell
cd "C:\Benchmark Final Projects\PersonalProject\QurkusProject\services\transaction-service"
.\mvnw.cmd quarkus:dev
```

### 3) budget-service (8083)

```powershell
cd "C:\Benchmark Final Projects\PersonalProject\QurkusProject\services\budget-service"
.\mvnw.cmd quarkus:dev
```

### 4) BFF (8080)

```powershell
cd "C:\Benchmark Final Projects\PersonalProject\QurkusProject\backend\bff"
.\mvnw.cmd quarkus:dev
```

Health checks (should show `UP`):
- `http://localhost:8080/q/health`
- `http://localhost:8081/q/health`
- `http://localhost:8082/q/health`
- `http://localhost:8083/q/health`

## Run the frontend

```powershell
cd "C:\Benchmark Final Projects\PersonalProject\QurkusProject\frontend"
npm install
npm run dev
```

Then open the URL Vite prints (usually `http://localhost:5173`).

> Note: the current frontend is a starter scaffold and is **not yet wired** to the BFF APIs.

## API testing (login → dashboard)

### 1) Login (get JWT)

- **POST** `http://localhost:8080/auth/login`
- Body (JSON):

```json
{ "username": "user", "password": "password" }
```

Response:

```json
{ "token": "eyJ..." }
```

### 2) Call dashboard summary

- **GET** `http://localhost:8080/dashboard/summary?month=2026-04`
- Header:
  - `Authorization: Bearer <token>`

This endpoint calls the 3 downstream services and returns an aggregated JSON response.

## Docker (do you need it?)

**No** for local dev/API testing: services use **H2 in-memory DB** in dev.

Docker (or local Postgres install) is only needed if you want to run using **production-like Postgres** configuration.

## Troubleshooting

- **Port already in use**
  - Find PID: `netstat -ano | findstr :8083` (replace `8083`)
  - Kill: `taskkill /PID <PID> /F`

- **BFF shows `service unavailable` warnings**
  - Ensure each service is running on `8081/8082/8083`
  - Ensure you are sending `Authorization: Bearer <token>` when calling `/dashboard/summary`

