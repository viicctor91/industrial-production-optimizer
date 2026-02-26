# Industrial Production Optimizer

Application for managing raw materials and products, plus a production suggestion feature that generates a deterministic (greedy) plan to maximize total sales value using the available stock.

## Tech Stack
- **Backend:** Java 21, **Quarkus 3**, RESTEasy Reactive, Hibernate ORM (Panache), Bean Validation
- **Frontend:** Vue 3 + Vite + TypeScript, Pinia, Vue Router, Axios, Tailwind CSS
- **Database:** Postgres (Local via Docker Compose by default) + optional Supabase Postgres
- **Tests:** JUnit 5 (backend), Vitest (frontend)
- **Extras:** OpenAPI + Swagger UI, i18n (pt-BR/en-US)

---

## Prerequisites
- Java **21**
- Maven **3.9+**
- Node.js **18+**
- Docker + Docker Compose

---

## Quickstart (Local - Recommended)
Runs everything locally with Docker Postgres (no Supabase credentials needed).

### 1) Start the database
```bash
docker compose up -d
```
PowerShell:
```powershell
docker compose up -d
```

### 2) Backend (Quarkus)
```bash
cd backend
mvn clean test
mvn quarkus:dev
```
PowerShell:
```powershell
cd backend
mvn clean test
mvn quarkus:dev
```
URLs:
- API: http://localhost:8080/api
- Swagger UI: http://localhost:8080/q/swagger-ui

### 3) Frontend (Vue/Vite)
```bash
cd frontend
npm install
npm run dev
```
PowerShell:
```powershell
cd frontend
npm install
npm run dev
```
URL:
- Frontend: http://localhost:5173

---

## Configuration
Default backend datasource connects to local Docker Postgres:
```
jdbc:postgresql://localhost:5432/optimizer
username=optimizer
password=optimizer
```
CORS for http://localhost:5173 is enabled. API base path: `/api`.

### Database credentials and auth tips (Postgres)
- Default container user: `optimizer` (no `postgres` role by default).
- Default password: `optimizer`.
- If you see “password authentication failed”:
  - Recreate DB: `docker compose down -v && docker compose up -d`
  - Or reset the password inside the container:
    - `docker exec -it optimizer-postgres psql -U optimizer -d optimizer -c "ALTER USER optimizer WITH PASSWORD 'optimizer';"`
  - Validate connection:
    - `docker exec -e PGPASSWORD=optimizer -it optimizer-postgres psql -U optimizer -d optimizer -c "\conninfo"`
  - Optional (force creds when starting backend):
    - PowerShell (session env):
      - `$env:QUARKUS_DATASOURCE_JDBC_URL='jdbc:postgresql://localhost:5432/optimizer'`
      - `$env:QUARKUS_DATASOURCE_USERNAME='optimizer'`
      - `$env:QUARKUS_DATASOURCE_PASSWORD='optimizer'`
      - `mvn quarkus:dev`

### Optional Supabase (Profile: supabase)
Set environment variables and run with profile:
```bash
QUARKUS_DATASOURCE_JDBC_URL='postgresql://<host>:<port>/<db>?sslmode=require'
QUARKUS_DATASOURCE_USERNAME='<user>'
QUARKUS_DATASOURCE_PASSWORD='<password>'
mvn quarkus:dev -Dquarkus.profile=supabase
```

---

## Seeds / Sample Data
Flyway migrations load seed data on startup:
- `backend/src/main/resources/db/migration/V1__init.sql`
- `backend/src/main/resources/db/migration/V2__seed.sql`

Seeds include:
- Raw Materials: Steel (100 kg), Plastic (80 kg), Screw (500 units)
- Products and composition: Steel Box, Plastic Case, Deluxe Box (conflict scenario ensured)

---

## Production Planner Algorithm
Greedy deterministic strategy:
1. Sort products by unit price (DESC), then by fewer composition items (ASC), then by product code (ASC).
2. Iteratively allocate max producible units based on remaining stock for each product.
3. Deduct consumed stock and accumulate total value.

---

## Tests
Backend:
```bash
cd backend
mvn clean test
```
Frontend:
```bash
cd frontend
npm test
```

---

## Troubleshooting
- Ensure Docker is running and port 5432 is free.
- If `mvn` is not recognized, install Maven 3.9+ and Java 21.
- If the frontend cannot reach the backend, verify that `VITE_API_BASE_URL` is `http://localhost:8080/api` (see `frontend/.env.example`).
- If Swagger UI is not available, start Quarkus with `mvn quarkus:dev` and ensure `quarkus.swagger-ui.always-include=true`.

---

## Optional convenience scripts
- Windows (PowerShell):
  - .\scripts\up.ps1
  - .\scripts\test.ps1
  - .\scripts\dev.ps1
- Linux/Mac (bash):
  - ./scripts/up.sh
  - ./scripts/test.sh
  - ./scripts/dev.sh
- Makefile (opcional):
  - make up
  - make test
  - make dev
  - Observação: no Windows, pode ser necessário instalar GNU Make.
