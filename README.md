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
| Caching | Spring Cache + Redis (Spring Data Redis) |
| Validation | spring-boot-starter-validation (Bean Validation 3) |
| Observability | Spring Boot Actuator |
| Build | Maven |
| Utilities | Lombok |

---

## Project Structure

```
src/main/java/com/expense/tracker/
├── config/
│   ├── CacheConfig.java       # RedisCacheManager: 8 named caches with per-cache TTL
│   └── SecurityConfig.java    # JWT filter chain, CORS, 401/403 handlers
├── controller/       # REST endpoints
├── service/          # Business logic + cache annotations
├── repository/       # JPA repositories
├── entity/           # JPA entities
├── dto/              # Request / response models (Bean Validation annotations)
├── mapper/           # Entity ↔ DTO mapping
├── security/         # KeycloakJwtAuthenticationConverter
├── exception/
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
│   ├── ErrorCode.java   # Module-scoped enum: SYS / TRX / USR / ATH / CAT / BDG / DSH
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

### Budgets
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/budgets/overall` | Get overall monthly budget |
| `POST` | `/api/budgets/overall` | Create overall monthly budget |
| `GET` | `/api/budgets/categories` | Get all category budgets for a month |
| `POST` | `/api/budgets/categories` | Create a category budget |
| `PUT` | `/api/budgets/{id}` | Update budget amount |
| `DELETE` | `/api/budgets/{id}` | Delete a budget |

### Dashboard & Summary
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/summary/dashboard` | Aggregate totals (income, expense, net) with MoM % change |
| `GET` | `/api/chart/dashboard/barchart` | 6-month income vs expense bar chart |
| `GET` | `/api/chart/dashboard/piechart` | Current month expense breakdown by category |

### Categories
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/categories` | All categories for the current user (system + custom) |

### Auth
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/auth/me` | Current user profile |

### System
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/actuator/health` | Health check (public) |

---

## Caching

Spring Cache backed by Redis. All cache keys are user-scoped via `jwt.subject`. Write operations use `@Caching` to evict multiple caches atomically. Strategy: write-invalidate.

| Cache | TTL | Invalidated by |
|---|---|---|
| `transactions` | 5 min | Transaction create / update / delete |
| `dashboard:summary` | 5 min | Transaction create / update / delete |
| `dashboard:bar-chart` | 10 min | Transaction create / update / delete |
| `dashboard:pie-chart` | 5 min | Transaction create / update / delete |
| `categories` | 30 min | — |
| `system-categories` | 24 h | — |
| `budgets:overall` | 10 min | Budget create / update / delete |
| `budgets:category` | 10 min | Budget create / update / delete |

---

## Exception Handling

All errors return a consistent `ApiErrorResponse`:

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

`ErrorCode` format: `{MODULE}-{HTTP_STATUS}{3-digit_seq}`. Spring Security 401/403 exceptions bypass `@ControllerAdvice` and are handled in `SecurityConfig.exceptionHandling()` — returning the same response shape.

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
| `V2.03` | Rename `budget` → `amount`; FK from budgets to categories with CASCADE |

---

## Security Model

- Stateless JWT authentication via Keycloak (RS256)
- `KeycloakJwtAuthenticationConverter` extracts `ROLE_USER` / `ROLE_ADMIN` from `realm_access.roles`
- All `/api/**` endpoints require `ROLE_USER`; `/api/admin/**` requires `ROLE_ADMIN`
- All queries scoped to authenticated user via `jwt.subject` — users cannot access each other's data
- No credentials in source — all sensitive config loaded from environment variables

---

## Testing

| Suite | Framework | Tests |
|---|---|---|
| `TransactionServiceTest` | JUnit 5 + Mockito | 7 — getById, create, update, delete (happy + not-found paths) |
| `TransactionControllerTest` | @WebMvcTest + MockMvc + spring-security-test | 8 — success, validation, and no-token paths |
| `TrackerApplicationTests` | @SpringBootTest + @ActiveProfiles("test") | Context loads without MySQL via test profile |

CI: GitHub Actions runs `mvn verify` on every pull request to `develop` and `master`.

---

## Configuration

| Profile | File | Purpose |
|---|---|---|
| `local` | `application-local.yml` | Local development |
| `test` | `src/test/resources/application.yml` | CI — Flyway + DataSource excluded |

### Key Environment Variables

| Variable | Default | Description |
|---|---|---|
| `SERVER_PORT` | `1331` | Spring Boot server port |
| `KEYCLOAK_URL` | `http://localhost:1880` | Keycloak base URL |
| `KEYCLOAK_REALM` | `expense-realm` | Keycloak realm |
| `DB_USERNAME` | — | Database username (required) |
| `DB_PASSWORD` | — | Database password (required) |
| `REDIS_HOST` | `localhost` | Redis host |
| `REDIS_PORT` | `6379` | Redis port |
| `CORS_ALLOWED_ORIGINS` | `http://localhost:5173` | Frontend origin for CORS |

---

## Completed Features

- [x] Transaction CRUD — user-scoped, multi-filter (type, category, month/year)
- [x] Budget CRUD — overall monthly + per-category, idempotent creation, conflict detection
- [x] Keycloak JWT authentication + role-based authorization (`ROLE_USER` / `ROLE_ADMIN`)
- [x] Auto user provisioning on first login
- [x] System categories + per-user hide/color preferences + custom user categories
- [x] Dashboard analytics: summary cards (MoM % change), 6-month bar chart, expense pie chart
- [x] Redis cache layer — 8 named caches, user-scoped keys, write-invalidate strategy
- [x] Enterprise exception hierarchy with module-scoped `ErrorCode` enum
- [x] Centralized `GlobalExceptionHandler` + SecurityConfig 401/403 handlers
- [x] Structured `ApiErrorResponse` with field-level violations
- [x] Flyway migrations V1.00 → V2.03
- [x] Unit tests: service layer (JUnit 5 + Mockito) + controller layer (@WebMvcTest + JWT injection)
- [x] GitHub Actions CI on PRs to `develop` / `master`
- [x] No credentials in source — full environment variable externalisation

---

## Roadmap

### Sprint 3 — M1 / M2 Close-out *(current)*
- [ ] Fix `CurrentUserService` readOnly transaction bug affecting first-login provisioning
- [ ] OpenAPI / Swagger documentation
- [ ] ADR: Redis cache design decisions
- [ ] Redis unavailability fallback verification
- [ ] Cache invalidation integration test
- [ ] Performance baseline — p95 latency on dashboard endpoints

### M3 — Async Events (Kafka)
- [ ] `TransactionCreated / Updated / Deleted` event publishing
- [ ] Async budget threshold alert consumer

### M4 — Search (Elasticsearch)
- [ ] Transaction index sync
- [ ] Full-text search on notes and remarks

### M5 — Cloud Native (Kubernetes + Prometheus + Grafana)
- [ ] Kubernetes manifests + Helm chart
- [ ] Prometheus metrics + Grafana dashboards
- [ ] GitHub Actions CD pipeline

---

## Local Development

### Prerequisites
- Java 21
- Maven
- Docker — see [infrastructure repo](https://github.com/JyzPoohs/expense-tracker-infra)

### Start infrastructure
```bash
# From expense-tracker-infra
cp .env.example .env
docker compose up -d    # MySQL + Keycloak + Redis
```

### Run the backend
```bash
cp .env.example .env
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

### Run tests
```bash
./mvnw verify
```

API: `http://localhost:1331`
