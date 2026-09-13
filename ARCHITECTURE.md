# ARCHITECTURE.md — CBTMS

> Full audit: see [ARCHITECTURE-AUDIT.md](ARCHITECTURE-AUDIT.md)  
> Module flows: see [MODULE-FLOW.md](MODULE-FLOW.md)  
> Database: see [DATABASE.md](DATABASE.md)  
> Implementation plan: see [IMPLEMENTATION-PLAN.md](IMPLEMENTATION-PLAN.md)

## System Overview

Corporate Business Travel Management System (CBTMS) — enterprise platform for end-to-end corporate travel lifecycle management.

```
┌─────────────┐     ┌──────────────────┐     ┌─────────────┐
│  React SPA  │────▶│  Spring Boot API │────▶│  MySQL 8.0  │
│  (Vite/TS)  │     │  (Java 17/Gradle)│     │  (Flyway)   │
└─────────────┘     └──────────────────┘     └─────────────┘
       │                     │
       │              ┌──────┴──────┐
       │              │  Providers  │
       │              │ (Mock/Real) │
       │              └─────────────┘
```

## Layers

| Layer | Package | Responsibility |
|-------|---------|----------------|
| Controller | `controller/` | HTTP, validation, DTO mapping |
| Service | `service/` | Business logic, transactions |
| Repository | `repository/` | JPA data access |
| Entity | `entity/` | Domain model |
| Security | `security/` | JWT, portal auth, company context |
| Provider | `provider/` | External service abstractions |

## Profiles

| Profile | Database | Schema | Use |
|---------|----------|--------|-----|
| `dev` (default) | MySQL | Hibernate `update` | Local development |
| `mysql` | MySQL | Flyway + `ddl-auto=none` | Production / Docker |
| `test` | H2 in-memory | Hibernate `create-drop` | Unit/integration tests |

## Portals (Frontend)

Seven separate React login portals sharing one backend auth system. See `frontend/src/auth/portals.ts`.

## Key Design Decisions

1. **Gradle over Maven** — existing codebase; migration deferred
2. **Company isolation via `CompanyContext`** — Phase 4 enforcement
3. **Provider pattern** — mock implementations for dev; real APIs later
4. **Flyway for production schema** — reproducible migrations
