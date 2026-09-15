# TorchEye Ledger — Backend

REST API service for a personal finance management application. Built with Spring Boot 3, secured with Keycloak OAuth2 JWT, and designed for incremental adoption of enterprise-grade infrastructure.

> **Repositories:** [Frontend](https://github.com/JyzPoohs/expense-tracker-react) · [Backend](https://github.com/JyzPoohs/expense-tracker) · [Infrastructure](https://github.com/JyzPoohs/expense-tracker-infra)

---

## Tech Stack

| Layer | Technology |
|---|---|
| Runtime | Java 21 |
| Framework | Spring Boot 3.3.5 |
| Security | Spring Security + OAuth2 Resource Server (Keycloak JWT) |
| Persistence | Spring Data JPA + MySQL 8 |
| Migrations | Flyway |
| Validation | spring-boot-starter-validation (Bean Validation 3) |
| API Docs | Springdoc OpenAPI (Swagger UI) |
| Observability | Spring Boot Actuator |
| Build | Maven |
| Utilities | Lombok |

---

## Project Structure

```
src/main/java/com/expense/tracker/
├── controller/       # REST endpoints
├── service/          # Business logic
├── repository/       # JPA repositories
├── entity/           # JPA entities
├── dto/              # Request / response models (Bean Validation annotations)
├── mapper/           # Entity ↔ DTO mapping
├── security/         # KeycloakJwtAuthenticationConverter, SecurityConfig
├── exception/        # Exception hierarchy + GlobalExceptionHandler
│   ├── AppException.java              # Abstract root
│   ├── ClientException.java           # Abstract 4xx
│   ├── ServerException.java           # Abstract 5xx
│   ├── ValidationException.java       # 400 + List<FieldViolation>
│   ├── ResourceNotFoundException.java # 404
│   ├── ConflictException.java         # 409
│   ├── SystemException.java           # 500 concrete
│   ├── GlobalExceptionHandler.java    # @RestControllerAdvice
│   ├── ApiErrorResponse.java          # Structured error body
│   └── FieldViolation.java            # Field-level error detail
├── constant/
│   ├── ErrorCode.java   # Module-scoped enum: 37 codes across SYS/TRX/USR/ATH/CAT/BDG/DSH
│   └── Role.java
└── utils/            # TransactionUtils
```

---

## API Endpoints

### Transactions
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/transactions` | Create a transaction |
| `GET` | `/api/transactions` | List transactions (filter: type, category, month, year) |
| `GET` | `/api/transactions/{id}` | Get transaction by ID |
| `PUT` | `/api/transactions/{id}` | Update transaction |
| `DELETE` | `/api/transactions/{id}` | Delete transaction |

### Dashboard & Summary
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/chart/dashboard/barchart` | 6-month income vs expense bar chart |
| `GET` | `/api/chart/dashboard/piechart` | Current month expense breakdown by category |
| `GET` | `/api/summary/dashboard` | Aggregate totals (income, expense, net) with filters |

### Categories
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/categories` | All categories for the current user (system + custom) |

### Auth
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/auth/me` | Current user profile (returns UserDTO) |

### System
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/actuator/health` | Health check (public) |
| `GET` | `/swagger-ui/index.html` | OpenAPI docs |

---

## Exception Handling

All errors return a consistent `ApiErrorResponse` body:

```json
{
  "code": "TRX-400001",
  "message": "Transaction validation failed",
  "status": 400,
  "error": "Bad Request",
  "path": "/api/transactions",
  "timestamp": "2026-09-15T10:30:00",
  "violations": [
    { "field": "amount", "message": "Amount must be greater than zero" }
  ]
}
```

`ErrorCode` format: `{MODULE}-{HTTP_STATUS}{3-digit_seq}`. Modules: `SYS` · `TRX` · `USR` · `ATH` · `CAT` · `BDG` · `DSH`.

---

## Database Schema

Managed by Flyway. Migrations in `src/main/resources/db/migration/`.

| Migration | Description |
|---|---|
| `V1.00` | Users table |
| `V1.01` | Categories table |
| `V1.02` | Transactions table |
| `V1.03` | System categories table |
| `V1.04` | System category preferences (per-user hide/color overrides) |
| `V1.05` | Seed data |
| `V2.00` | Budgets table (user_id, category_id, amount, month, year) |
| `V2.01` | Indexes and constraints on system categories |
| `V2.02` *(planned)* | UNIQUE constraint on budgets (user_id, category_id, month, year) |

---

## Security Model

- Stateless JWT authentication via Keycloak (RS256)
- `KeycloakJwtAuthenticationConverter` extracts `ROLE_USER` / `ROLE_ADMIN` from `realm_access.roles`
- All `/api/**` endpoints require `ROLE_USER`
- All sensitive config (DB credentials, Keycloak secrets) loaded from environment variables — no secrets in source
- CORS origin configured via `CORS_ALLOWED_ORIGIN` env var

---

## Configuration

| Profile | File | Purpose |
|---|---|---|
| `local` | `application-local.yml` | Local development with `.env` file support |
| `prod` | `application-prod.yml` | Production (no debug logging) |

### Key Environment Variables

| Variable | Default | Description |
|---|---|---|
| `SERVER_PORT` | `1331` | Spring Boot server port |
| `MYSQL_PORT` | `3307` | MySQL host port |
| `KEYCLOAK_URL` | `http://localhost:1880` | Keycloak base URL |
| `KEYCLOAK_REALM` | `expense-realm` | Keycloak realm |
| `DB_USERNAME` | — | Database username (required) |
| `DB_PASSWORD` | — | Database password (required) |
| `CORS_ALLOWED_ORIGIN` | `http://localhost:5173` | Frontend origin for CORS |

---

## Completed Features

- [x] Transaction CRUD with multi-filter support (type, category, month/year, user-scoped)
- [x] Keycloak JWT authentication + role-based authorization
- [x] Auto user provisioning on first login
- [x] System categories + user custom categories + per-user preferences
- [x] Dashboard analytics: bar chart, pie chart, summary cards
- [x] Enterprise exception hierarchy: `AppException → ClientException / ServerException`
- [x] Module-scoped `ErrorCode` enum (37 codes across 7 modules)
- [x] Bean Validation on all request DTOs (`@Valid`, `@NotBlank`, `@Positive`, etc.)
- [x] `GlobalExceptionHandler` with 5 targeted handlers + catch-all
- [x] Structured `ApiErrorResponse` with field-level `FieldViolation` list
- [x] Flyway database migrations (V1.00 → V2.01)
- [x] Swagger / OpenAPI documentation
- [x] Spring Boot Actuator health endpoint
- [x] Environment variable externalisation — no credentials in source

---

## Roadmap

### Sprint 2 — Feature Completion & Test Baseline *(current)*
- [ ] Budget Java layer: entity, Flyway V2.02, repository, DTO, service, controller (KAN-44)
- [ ] TransactionService unit tests — JUnit 5 + Mockito (KAN-81)
- [ ] GlobalExceptionHandler integration tests — @WebMvcTest (KAN-81)

### Phase 2 — Budget Management
- [ ] Per-category monthly budget limits UI
- [ ] Budget vs actual spending comparison API
- [ ] Budget progress bars + over-budget alerts

### Phase 3 — Caching (Redis)
- [ ] `spring-boot-starter-data-redis` + connection config
- [ ] Cache dashboard chart and summary responses with TTL
- [ ] Cache invalidation on transaction write

### Phase 4 — Search (Elasticsearch)
- [ ] Sync transactions to Elasticsearch
- [ ] Full-text search on notes and remarks

### Phase 5 — Event Streaming (Kafka)
- [ ] Publish `TransactionCreated / Updated / Deleted` events
- [ ] Async budget threshold alert consumer

### Phase 6 — AI Financial Advisor
- [ ] Monthly spending analysis endpoint
- [ ] LLM-powered per-category breakdown and savings suggestions

### Phase 7 — Production Hardening
- [ ] Pagination on `GET /api/transactions`
- [ ] JUnit 5 + Testcontainers integration tests (KAN-73)
- [ ] Structured JSON logging for log aggregation
- [ ] Rate limiting on public API endpoints

### Phase 8 — Kubernetes
- [ ] Dockerfile for Spring Boot service
- [ ] Kubernetes manifests + Helm chart
- [ ] GitHub Actions CI/CD pipeline

---

## Local Development

### Prerequisites
- Java 21
- Maven
- Docker (for infra services — see [infrastructure repo](https://github.com/JyzPoohs/expense-tracker-infra))

### Start infrastructure
```bash
# From expense-tracker-infra
cp .env.example .env   # fill in secrets
docker compose up -d
```

### Run the backend
```bash
cp .env.example .env   # fill in DB_USERNAME, DB_PASSWORD, etc.
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

API: `http://localhost:1331`
Swagger UI: `http://localhost:1331/swagger-ui/index.html`
