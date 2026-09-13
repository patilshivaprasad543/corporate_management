# ARCHITECTURE-AUDIT.md — Corporate Business Travel Management System (CBTMS)

**Audit date:** 2026-09-13  
**Branch:** `cursor/cbtms-phases-0-2-8bcd`

---

## 1. Executive Summary

The repository contains a **partially implemented** enterprise travel platform with a working Spring Boot backend, React portal frontend, and extensive domain entities. It is **not yet** a complete end-to-end CBTMS matching the master specification. Authentication portals, basic travel/expense modules, and mock providers exist; Flyway, company isolation, state machines, and full workflow connectivity are incomplete.

---

## 2. Technology Stack — Spec vs Actual

| Area | Master Spec | Current Reality | Gap |
|------|-------------|-----------------|-----|
| Backend build | Maven | **Gradle** (`build.gradle`, `./gradlew`) | Medium |
| Java version | 21 | **17** (toolchain in build.gradle) | Medium |
| Database (prod) | MySQL 8+ only | MySQL configured; **H2 used for tests/dev** | High |
| Schema management | Flyway | **Hibernate `ddl-auto=update`** | High |
| Frontend | React+TS+Vite+Tailwind+TanStack Query+Recharts | React+TS+Vite+Tailwind; **no TanStack Query/Recharts** | Medium |
| Mail | Spring Mail | **Not in dependencies** | High |
| E2E tests | Playwright | **None** | High |
| Integration tests | Testcontainers | **H2 in-memory only** | High |

**Decision for Phases 0–2:** Extend existing Gradle/Java 17 stack (avoid breaking rewrite). Add Flyway, Spring Mail, frontend deps. Document Maven/Java 21 migration in IMPLEMENTATION-PLAN.md Phase 29+.

---

## 3. Repository Structure

```
/workspace
├── src/main/java/com/corporate/travel/   # Spring Boot backend
│   ├── controller/     (12 REST controllers)
│   ├── service/        (20+ services)
│   ├── entity/         (35 JPA entities)
│   ├── repository/     (JPA repositories)
│   ├── security/       (JWT, SecurityConfig)
│   ├── provider/       (Flight, Hotel, OTP mocks)
│   ├── ai/             (AI assistant services)
│   └── config/         (WebMvc, OpenAPI, WebSocket)
├── src/main/resources/
│   ├── application.properties
│   └── static/         (legacy vanilla JS SPA — superseded by /frontend)
├── src/test/java/      (9 test classes, 28 tests)
├── frontend/           (React+Vite+TS portal auth UI)
├── database/schema.sql (legacy DDL — not aligned with JPA)
├── docker-compose.yml  (MySQL + backend)
├── Dockerfile          (backend multi-stage)
└── .cursor/            (Cloud Agent environment config)
```

---

## 4. Backend Modules — Existing

| Module | Controller | Service | Entity | Status |
|--------|-----------|---------|--------|--------|
| Authentication | AuthController | AuthService, OtpService, RefreshTokenService | User, Role, Permission, RefreshToken, OtpVerification | **Portal auth done** |
| Travel Requests | TravelRequestController | TravelRequestService | TravelRequest, ApprovalStep | Partial — no state machine service |
| Approvals | ApprovalController | ApprovalWorkflowService | ApprovalStep | Partial — not dynamic engine |
| Policy | — | PolicyEvaluationService | TravelPolicy, PolicyRule | Basic evaluation |
| Flight/Hotel/Transport Search | TravelSearchController | TravelSearchService | Flight, Hotel, Transportation | Mock search |
| Bookings | BookingController | BookingService | Booking, BookingItem | Partial |
| Expenses | ExpenseController | ExpenseService | ExpenseReport, ExpenseItem | Partial |
| Analytics | AnalyticsController | AnalyticsService | — | Mock/aggregated data |
| Risk | RiskController | RiskManagementService | RiskAlert | Mock |
| Notifications | NotificationController | NotificationService | Notification | Basic |
| Audit | AuditController | AuditService | AuditLog | Basic |
| AI | AIController | AITravelAssistantService, etc. | — | Mock (no Ollama) |
| Chat/Support | ChatController | ChatService | ChatConversation, ChatMessage | Basic |
| Sustainability | — | SustainabilityService | CarbonRecord | Partial |

**Missing controllers/APIs (per spec):** `/api/companies`, `/api/departments`, `/api/employees`, `/api/policies`, `/api/trips`, `/api/itineraries`, `/api/documents`, `/api/reimbursements`, `/api/invoices`, `/api/payments`, `/api/refunds`, `/api/vendors`, `/api/agents`, `/api/support`, `/api/reports`, `/api/admin`.

---

## 5. Frontend — Existing

| Feature | Status |
|---------|--------|
| 7 separate login portals | ✅ Implemented (`/employee/login`, etc.) |
| Route guards per portal | ✅ AuthContext + guards |
| Registration + OTP pages | ✅ `/register`, `/verify-otp` |
| Role dashboards | ⚠️ Shell only (user info, no real counts) |
| Travel request UI | ❌ Not in React frontend |
| TanStack Query | ❌ Missing |
| Recharts | ❌ Missing |
| Reusable DataTable, etc. | ❌ Missing |

Legacy `src/main/resources/static/` vanilla JS app still exists (CorporateTravel360 SPA).

---

## 6. Database — Existing

- **35 JPA entities** mapped to MySQL-compatible tables
- **No Flyway** — schema managed by Hibernate `ddl-auto=update`
- `database/schema.sql` is **outdated** (different column names than JPA entities)
- Auth tables added recently: `permissions`, `role_permissions`, `refresh_tokens`, `otp_verifications`, `password_reset_tokens`

### Company isolation
- `Organization` entity exists; `User.organization_id` FK present
- **No `CompanyContext` / tenant filter** — cross-company access not systematically blocked

---

## 7. Security — Existing

| Feature | Status |
|---------|--------|
| JWT access tokens | ✅ |
| Refresh tokens | ✅ |
| Portal validation on login | ✅ |
| Granular permissions (DB) | ✅ Seeded |
| Permission enforcement on APIs | ⚠️ Role-based URL rules only |
| Company-scoped queries | ❌ |
| Rate limiting | ❌ |
| File upload validation | ⚠️ Partial |

---

## 8. State Machines — Gap Analysis

### Travel Request Status (current `RequestStatus`)
```
DRAFT, SUBMITTED, MANAGER_REVIEW, FINANCE_REVIEW, APPROVED, REJECTED,
MODIFICATION_REQUESTED, BOOKING_IN_PROGRESS, CONFIRMED, TRAVEL_ACTIVE,
COMPLETED, EXPENSED, CLOSED
```

### Required (spec)
```
DRAFT, SUBMITTED, UNDER_REVIEW, MANAGER_APPROVED, TRAVEL_ADMIN_APPROVED,
FINANCE_APPROVED, BOOKING_AUTHORIZED, BOOKED, IN_PROGRESS, COMPLETED,
REJECTED, CANCELLED, MODIFICATION_REQUIRED
```

**No `TravelRequestStateMachineService` exists.** Illegal transitions are not prevented.

---

## 9. Provider Abstractions — Existing

| Provider | Interface | Implementation |
|----------|-----------|----------------|
| Flight | FlightProvider | DefaultTravelProviderService (mock) |
| Hotel | HotelProvider | DefaultTravelProviderService (mock) |
| Receipt OCR | ReceiptOcrProvider | DefaultTravelProviderService (mock) |
| Exchange Rate | ExchangeRateProvider | DefaultTravelProviderService (mock) |
| OTP | OtpProvider | MockOtpProvider / EmailOtpProvider |
| Payment | — | ❌ Missing |
| Email | — | ❌ Missing |
| Risk | — | Inline in RiskManagementService |
| AI | — | AITravelAssistantService (mock, no Ollama) |

---

## 10. Tests — Existing

| Type | Count | Notes |
|------|-------|-------|
| Controller tests | 6 | Auth, Analytics, AI, Expense, TravelSearch |
| Integration tests | 2 | TravelWorkflow, PortalSecurity (14 scenarios) |
| Service tests | 1 | PolicyEvaluation |
| Unit tests | 0 dedicated | — |
| Frontend tests | 0 | No Vitest setup |
| E2E (Playwright) | 0 | — |
| Testcontainers | 0 | H2 only |

**All 28 backend tests passing** (as of auth portal PR).

---

## 11. Docker / Deployment

| Component | Status |
|-----------|--------|
| Backend Dockerfile | ✅ Multi-stage Java 17 |
| docker-compose MySQL | ✅ |
| docker-compose backend | ✅ |
| Frontend Dockerfile | ❌ (Phase 1) |
| Frontend in compose | ❌ (Phase 1) |
| `.env.example` | ✅ Partial |

---

## 12. Documentation — Existing

| Document | Status |
|----------|--------|
| README.md | ✅ Basic |
| AUTHENTICATION-AUDIT.md | ✅ Auth-specific |
| ARCHITECTURE.md | ❌ (Phase 1) |
| DATABASE.md | ❌ (Phase 0) |
| MODULE-FLOW.md | ❌ (Phase 0) |
| IMPLEMENTATION-PLAN.md | ❌ (Phase 0) |
| API.md, SECURITY.md, etc. | ❌ Later phases |

---

## 13. Critical Risks

1. **Schema drift** — JPA entities vs `database/schema.sql` vs Hibernate auto-update
2. **No company isolation** — IDOR risk across organizations
3. **No state machine** — workflow integrity not enforced
4. **Dual frontends** — React portals vs legacy static SPA confusion
5. **Mock analytics** — dashboard numbers not always from DB
6. **No Flyway** — non-reproducible schema across environments

---

## 14. Recommended Phase Order (see IMPLEMENTATION-PLAN.md)

Phases 0–2 (this PR) establish audit, foundation, and Flyway+MySQL. Phases 3–29 implement full workflow per master spec.
