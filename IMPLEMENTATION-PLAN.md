# IMPLEMENTATION-PLAN.md — CBTMS Phased Delivery

## Principles

1. **Never skip verification** — compile, test, build after each phase
2. **Extend, don't rewrite** — reuse existing Gradle/Spring Boot codebase
3. **Real workflows only** — mock providers behind interfaces, real business logic
4. **Backend enforces everything** — frontend is presentation only

---

## Phase Status Tracker

| Phase | Name | Status | Branch |
|-------|------|--------|--------|
| 0 | Workspace Audit | ✅ Complete | cursor/cbtms-phases-0-2-8bcd |
| 1 | Project Foundation | ✅ Complete | cursor/cbtms-phases-0-2-8bcd |
| 2 | Database + Flyway + JPA | ✅ Complete | cursor/cbtms-phases-0-2-8bcd |
| 3 | Auth + OTP + JWT + RBAC (enhance) | ✅ Complete | cursor/cbtms-phase-3-auth-8bcd |
| 4 | Company + Department + Employee | ⏳ Pending | — |
| 5 | Travel Policy Engine | ⏳ Pending | — |
| 6 | Travel Request + State Machine | ⏳ Pending | — |
| 7 | Approval Engine | ⏳ Pending | — |
| 8–10 | Flight / Hotel / Transport | ⏳ Pending | — |
| 11 | Booking + Cancel + Rebook | ⏳ Pending | — |
| 12 | Trip + Itinerary | ⏳ Pending | — |
| 13–16 | Documents, Risk, Expense, Payment | ⏳ Pending | — |
| 17–18 | Vendor, Travel Agent | ⏳ Pending | — |
| 19–20 | Notifications, Support | ⏳ Pending | — |
| 21–23 | AI, Sustainability, Analytics | ⏳ Pending | — |
| 24 | Complete Frontend Integration | ⏳ Pending | — |
| 25 | Automated Testing (Playwright, Testcontainers) | ⏳ Pending | — |
| 26 | Docker + Deployment | ⏳ Pending | — |
| 27–29 | Security, Performance, E2E Audit | ⏳ Pending | — |

---

## Phase 0 — Workspace Audit ✅

**Deliverables:**
- [x] ARCHITECTURE-AUDIT.md
- [x] MODULE-FLOW.md
- [x] DATABASE.md
- [x] IMPLEMENTATION-PLAN.md

---

## Phase 1 — Project Foundation

**Goals:** Standardize project structure, add missing dependencies, Docker for full stack.

**Tasks:**
- [x] Add Flyway, Spring Mail to `build.gradle`
- [x] Add TanStack Query, Recharts to frontend
- [x] Create `application-mysql.properties` profile
- [x] Create `frontend/Dockerfile`
- [x] Update `docker-compose.yml` with frontend service
- [x] Update `.env.example` with all config keys
- [x] Create `ARCHITECTURE.md` (summary pointer)
- [x] Add `CompanyContext` holder (empty, for Phase 4)
- [x] Configure QueryClientProvider in React

**Verification:**
- `./gradlew compileJava` ✅
- `cd frontend && npm run build` ✅
- All 29 tests pass ✅

---

## Phase 2 — Database + Flyway + JPA

**Goals:** Replace Hibernate auto-DDL with Flyway for MySQL; validate schema.

**Tasks:**
- [x] Create `db/migration/V1__auth_and_rbac.sql`
- [x] Create `db/migration/V2__organization_structure.sql`
- [x] Create `db/migration/V3__travel_domain.sql`
- [x] Create `db/migration/V4__booking_and_trip.sql`
- [x] Create `db/migration/V5__finance_and_operations.sql`
- [x] Configure Flyway in `application-mysql.properties`
- [x] Set `spring.jpa.hibernate.ddl-auto=none` for mysql profile (Flyway owns schema)
- [x] Keep test profile: H2 + `ddl-auto=create-drop` + `flyway.enabled=false`
- [x] Add `FlywayConfigurationTest` (context loads with test profile)

**Verification:**
- `./gradlew test` (29 tests) ✅
- `./gradlew bootRun --args='--spring.profiles.active=mysql'` against docker MySQL — pending (Docker unavailable in cloud agent VM)

---

## Phase 3 — Auth Enhancement ✅

**Delivered:**
- [x] Company-scoped login validation (`organizationId` required for all portals except Super Admin)
- [x] `SecurityAuthorizationService` + permission authorities on `UserPrincipal`
- [x] `@PreAuthorize` on travel, approval, expense, booking, audit, analytics controllers
- [x] Employee registration with company, department, employee ID, domain validation
- [x] Public `/api/auth/companies` and `/api/auth/companies/{id}/departments` endpoints
- [x] OTP daily resend limit (DB-backed) + cooldown
- [x] `CompanyContext` populated from JWT filter
- [x] JWT `organizationId` claim
- [x] `JwtAuthenticationEntryPoint` (401) + `JwtAccessDeniedHandler` (403)
- [x] `AuthSecurityTest` — 9 new security scenarios (40 tests total)
- [x] Frontend: company selector on login, full registration form

---

## Phase 4 — Company Module (Next)

- `CompanyController` / `CompanyService`
- Super Admin company CRUD APIs
- `CompanyContext` filter from JWT
- `TenantAwareRepository` base or spec filters
- React Super Admin company management UI

---

## Dependency Order (Critical Path)

```
Phase 2 (Flyway)
  → Phase 3 (Auth hardening)
    → Phase 4 (Company isolation) ← BLOCKS all tenant data access
      → Phase 5 (Policy)
        → Phase 6 (Travel Request + State Machine)
          → Phase 7 (Approval Engine)
            → Phases 8-11 (Search + Booking)
              → Phase 12 (Trip)
                → Phases 13-16 (Docs, Risk, Expense, Payment)
                  → Phases 19-23 (Notify, Analytics)
                    → Phase 24 (Frontend integration)
                      → Phase 25-29 (Testing, Docker, Audit)
```

**Do not implement booking UI before approval engine is complete.**  
**Do not implement analytics before expense/payment data flows exist.**

---

## Known Deviations from Master Spec (Documented)

| Spec | Current Plan |
|------|--------------|
| Maven | Keep Gradle until Phase 29 migration assessment |
| Java 21 | Keep Java 17 until Phase 29 |
| H2 prohibited | H2 retained for unit tests only; MySQL for all other profiles |
| All 48 entity tables in spec | ~35 exist; remainder added per phase |
