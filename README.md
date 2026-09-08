# TorchEye Ledger — Backend

REST API service for a personal finance management application. Built with Spring Boot 3, secured with Keycloak OAuth2, and designed for incremental adoption of enterprise-grade infrastructure.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Runtime | Java 21 |
| Framework | Spring Boot 3.3.5 |
| Security | Spring Security + OAuth2 Resource Server (Keycloak JWT) |
| Persistence | Spring Data JPA + MySQL 8 |
| Migrations | Flyway |
| API Docs | Springdoc OpenAPI (Swagger UI) |
| Observability | Spring Boot Actuator |
| Build | Maven |
| Utilities | Lombok |

---

## Architecture

```
expense-tracker/
└── tracker/
    └── src/main/java/com/expense/tracker/
        ├── controller/       # REST endpoints
        ├── service/          # Business logic
        ├── repository/       # JPA repositories
        ├── entity/           # JPA entities
        ├── dto/              # Request/response models
        ├── mapper/           # Entity ↔ DTO mapping
        ├── security/         # Keycloak JWT converter, SecurityConfig
        ├── exception/        # Global exception handler, error codes
        ├── constant/         # Enums: Role, TransactionType, Category enums
        └── utils/            # TransactionUtils helpers
```

The service runs on port `8081`. Keycloak runs on port `8080` (managed by the infra repo).

---

## API Endpoints

### Transactions
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/transactions` | Create a transaction |
| `GET` | `/api/transactions` | List transactions (filterable by type, category, month, year) |
| `GET` | `/api/transactions/{id}` | Get transaction by ID |
| `PUT` | `/api/transactions/{id}` | Update transaction |
| `DELETE` | `/api/transactions/{id}` | Delete transaction |

### Dashboard
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/chart/dashboard/barchart` | 6-month income vs expense bar chart data |
| `GET` | `/api/chart/dashboard/piechart` | Current month expense breakdown by category |
| `GET` | `/api/summary/dashboard` | Aggregate totals (income, expense, net) with filters |

### Categories
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/categories` | Get all categories for the current user (system + custom) |

### Auth
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/auth/provision` | Auto-provision user on first login from Keycloak JWT |

### System
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/actuator/health` | Health check (public) |
| `GET` | `/swagger-ui/index.html` | OpenAPI documentation |

---

## Database Schema

Managed by Flyway. Migrations in `src/main/resources/db/migration/`.

| Migration | Description |
|---|---|
| `V1.00` | Users table |
| `V1.01` | Categories table (user-defined) |
| `V1.02` | Transactions table |
| `V1.03` | System categories table |
| `V1.04` | System category preferences table (per-user hide/color overrides) |
| `V1.05` | Seed data |
| `V2.00` | Budgets table (schema ready, service not yet implemented) |
| `V2.01` | Additional constraints |

---

## Security

- Stateless JWT authentication via Keycloak
- Custom `KeycloakJwtAuthenticationConverter` extracts roles from Keycloak's `realm_access.roles`
- Role-based access: `ROLE_USER` for all `/api/**`, `ROLE_ADMIN` for `/api/admin/**`
- CORS configured for frontend origin

---

## Configuration

Two active profiles:

| Profile | File | Use case |
|---|---|---|
| `local` | `application-local.yml` | Local development |
| `prod` | `application-prod.yml` | Production deployment |

---

## Completed Features

- [x] Transaction CRUD with full filter support (type, category, date range)
- [x] Multi-user isolation — all queries scoped to authenticated user
- [x] Keycloak JWT authentication + role-based authorization
- [x] Auto user provisioning on first login
- [x] System categories + user custom categories
- [x] Per-user system category preferences (hide categories, custom colors)
- [x] Dashboard analytics: 6-month bar chart, monthly pie chart by category
- [x] Dashboard summary cards (total income / expense / net)
- [x] Flyway database migrations
- [x] Global exception handling with structured error codes
- [x] Swagger / OpenAPI documentation
- [x] Spring Boot Actuator health endpoint
- [x] Budget table schema (DB migration exists)

---

## Roadmap

### Phase 2 — Budget Management
- [ ] Budget entity, service, controller (schema already exists in V2.00)
- [ ] Per-category monthly budget limits
- [ ] Budget vs actual spending comparison API

### Phase 3 — Caching (Redis)
- [ ] Add `spring-boot-starter-data-redis` (dependency already scaffolded, currently commented out)
- [ ] Cache dashboard chart and summary responses with TTL
- [ ] Invalidate cache on transaction write (create / update / delete)
- [ ] Cache user session/profile data

### Phase 4 — Search (Elasticsearch)
- [ ] Sync transactions to Elasticsearch via event pipeline
- [ ] Full-text search across notes and remarks
- [ ] Advanced aggregation queries for analytics

### Phase 5 — Event Streaming (Kafka)
- [ ] Publish `TransactionCreated`, `TransactionUpdated`, `TransactionDeleted` events
- [ ] Async notification consumer (e.g., budget threshold alerts)
- [ ] AI analysis pipeline consumer

### Phase 6 — AI Financial Advisor
- [ ] Monthly spending analysis endpoint
- [ ] LLM-powered advice: per-category breakdown + savings suggestions
- [ ] Proactive alert when spending exceeds budget or historical average

### Phase 7 — Production Hardening
- [ ] Pagination on `GET /api/transactions` (currently returns all records)
- [ ] Fix `getByType` / `getByCategory` endpoints (missing userId filter — data leak risk)
- [ ] Unit and integration tests (JUnit 5 + Testcontainers)
- [ ] Replace hardcoded CORS origin with environment config
- [ ] Rate limiting
- [ ] Structured logging (JSON format for log aggregation)

### Phase 8 — Kubernetes
- [ ] Dockerfile for the Spring Boot service
- [ ] Kubernetes manifests (Deployment, Service, ConfigMap, Secret)
- [ ] Helm chart
- [ ] Horizontal Pod Autoscaler
- [ ] Health probe configuration (liveness / readiness via Actuator)

---

## Local Development

### Prerequisites
- Java 21
- Maven
- Docker (for infra services)

### Start infrastructure
```bash
# From the expense-tracker-infra repo
docker compose up -d
```

### Run the application
```bash
./mvnw spring-boot:run
```

The API is available at `http://localhost:8081`.
Swagger UI: `http://localhost:8081/swagger-ui/index.html`
