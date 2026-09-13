# Authentication Audit — CorporateTravel360

**Date:** 2026-09-13  
**Auditor:** Cloud Agent (pre-implementation)

## Stack Reality vs. Prompt

| Area | Prompt Spec | Actual Project |
| --- | --- | --- |
| Backend | Java 21, Maven | **Java 17, Gradle** |
| Frontend | React + Vite + TypeScript | **Vanilla HTML/JS** in `src/main/resources/static/` |
| Database | MySQL 8+ | **MySQL configured**; H2 used for tests/local dev |
| Build | Maven | **Gradle** (`build.gradle`, `./gradlew`) |

This implementation **extends the existing Gradle/Spring Boot backend** and **adds a React/Vite/TypeScript frontend** rather than replacing the working backend.

---

## Backend Structure

```
src/main/java/com/corporate/travel/
├── controller/AuthController.java      # POST /login, /register, GET /me
├── service/AuthService.java            # Login + registration logic
├── service/SeedDataService.java        # Seeds 9 roles + demo users
├── service/AuditService.java           # audit_logs persistence
├── security/
│   ├── SecurityConfig.java             # JWT filter chain, role-based URL rules
│   ├── JwtTokenProvider.java           # Access token only (no refresh)
│   ├── JwtAuthenticationFilter.java
│   ├── CustomUserDetailsService.java
│   └── UserPrincipal.java
├── entity/User.java                    # users table
├── entity/Role.java                    # roles table (RoleType enum)
└── entity/enums/RoleType.java          # 9 Spring Security roles
```

## Existing Roles (`RoleType`)

| RoleType | Seed User | Email |
| --- | --- | --- |
| `ROLE_SUPER_ADMIN` | superadmin | superadmin@corporatetravel.com |
| `ROLE_COMPANY_ADMIN` | admin | admin@acmetech.com |
| `ROLE_TRAVEL_MANAGER` | travelmgr | travelmgr@acmetech.com |
| `ROLE_APPROVER` | manager | manager@acmetech.com |
| `ROLE_EMPLOYEE` | employee | traveler@acmetech.com |
| `ROLE_FINANCE` | finance | finance@acmetech.com |
| `ROLE_HR` | hr | hr@acmetech.com |
| `ROLE_VENDOR` | vendor | partner@indigoair.com |
| `ROLE_SUPPORT` | support | support@corporatetravel.com |

**Password for all seed users:** `password123`

## Existing Authentication Flow

1. `POST /api/auth/login` with `{ usernameOrEmail, password }` — **no portal validation**
2. Spring `AuthenticationManager` validates credentials via BCrypt
3. JWT access token issued (24h default); **no refresh token**
4. `GET /api/auth/me` returns authenticated profile
5. `POST /api/auth/register` creates `ROLE_EMPLOYEE` only; sets `emailVerified=false` but still issues JWT immediately

## Gaps Identified (Pre-Implementation)

| Requirement | Status Before |
| --- | --- |
| Separate login portals per role | ❌ Single SPA with role switcher |
| Portal validation on login | ❌ Any role can login anywhere |
| Granular permissions | ❌ Role-only authorization |
| Refresh tokens | ❌ Not implemented |
| Logout endpoint | ❌ Not implemented |
| OTP email verification | ❌ Flag exists, no OTP flow |
| Password reset tokens | ❌ Not implemented |
| `refresh_tokens` table | ❌ Missing |
| `permissions` / `role_permissions` | ❌ Missing |
| React frontend | ❌ Vanilla JS only |
| Portal security tests | ❌ Only basic login test |
| Company isolation enforcement | ⚠️ `organization_id` on User; not enforced in all services |
| `last_login_at` tracking | ❌ Missing |

## Database Tables (Existing)

- `users` — username, email, password (BCrypt), first/last name, phone, active, email_verified, organization_id
- `roles` — RoleType enum
- `user_roles` — many-to-many
- `audit_logs` — action logging via AuditService

## Existing Tests

- `AuthControllerTest` — valid/invalid password login
- `TravelWorkflowIntegrationTest` — employee login + travel request

## Implementation Plan

1. Add `PortalType` enum mapping portals → existing `RoleType`
2. Add `Permission` entity + `role_permissions` + seed data
3. Add `RefreshToken`, `OtpVerification`, `PasswordResetToken` entities
4. Extend `AuthService` with portal validation, refresh, logout, OTP registration
5. Create React/Vite/TypeScript frontend with 7 separate login portals + route guards
6. Add `PortalSecurityTest` for cross-portal denial scenarios
