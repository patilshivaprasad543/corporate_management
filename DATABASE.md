# DATABASE.md — CBTMS Schema Reference

## Database Engine

- **Production:** MySQL 8.0+ (`corporate_db`)
- **Schema management:** Flyway migrations (`src/main/resources/db/migration/`)
- **Tests:** H2 in-memory (profile `test`, Flyway disabled, `ddl-auto=create-drop`)

## Naming Conventions

| Convention | Example |
|------------|---------|
| Tables | `snake_case` plural (`travel_requests`) |
| PK | `id BIGINT AUTO_INCREMENT` |
| FK | `{entity}_id` |
| Audit | `created_at`, `updated_at`, `is_deleted` |
| Optimistic lock | `version BIGINT` on `users` |
| Status | `VARCHAR` enum string |
| Money | `DECIMAL(15,2)` |

---

## Entity Groups

### Auth & RBAC (Phase 2 — V1)
| Table | Entity | Key FKs |
|-------|--------|---------|
| `users` | User | `organization_id` → organizations |
| `roles` | Role | — |
| `permissions` | Permission | — |
| `user_roles` | User↔Role | `user_id`, `role_id` |
| `role_permissions` | Role↔Permission | `role_id`, `permission_id` |
| `refresh_tokens` | RefreshToken | `user_id` |
| `otp_verifications` | OtpVerification | `user_id` |
| `password_reset_tokens` | PasswordResetToken | `user_id` |

### Organization (Phase 2 — V2)
| Table | Entity | Key FKs |
|-------|--------|---------|
| `organizations` | Organization | — |
| `departments` | Department | `organization_id` |
| `cost_centers` | CostCenter | `organization_id`, `department_id` |
| `employee_profiles` | EmployeeProfile | `user_id`, `department_id`, `cost_center_id` |

### Travel Domain (Phase 2 — V3)
| Table | Entity | Key FKs |
|-------|--------|---------|
| `travel_policies` | TravelPolicy | `organization_id` |
| `policy_rules` | PolicyRule | `policy_id` |
| `travel_requests` | TravelRequest | `employee_id`, `organization_id`, `department_id`, `cost_center_id` |
| `approval_steps` | ApprovalStep | `travel_request_id`, `approver_id` |

### Booking & Trip (Phase 2 — V4)
| Table | Entity |
|-------|--------|
| `bookings`, `booking_items` | Booking, BookingItem |
| `flights`, `hotels`, `transportation` | Flight, Hotel, Transportation |
| `itineraries`, `itinerary_events` | Itinerary, ItineraryEvent |

### Finance & Operations (Phase 2 — V5)
| Table | Entity |
|-------|--------|
| `expense_reports`, `expense_items` | ExpenseReport, ExpenseItem |
| `invoices`, `payment_transactions` | Invoice, PaymentTransaction |
| `travel_wallets`, `corporate_cards` | TravelWallet, CorporateCard |
| `vendors` | Vendor |
| `notifications`, `audit_logs`, `risk_alerts` | Notification, AuditLog, RiskAlert |
| `carbon_records` | CarbonRecord |
| `chat_conversations`, `chat_messages` | ChatConversation, ChatMessage |

---

## Company Isolation Rule

Every tenant-owned row MUST include `organization_id` (or inherit via FK chain):

```
users.organization_id
  → travel_requests.organization_id
  → bookings → travel_requests.organization_id
  → expense_reports → user.organization_id
```

Queries MUST include: `WHERE organization_id = :currentOrgId`

---

## Flyway Migration Files

| Version | File | Description |
|---------|------|-------------|
| V1 | `V1__auth_and_rbac.sql` | Users, roles, permissions, tokens |
| V2 | `V2__organization_structure.sql` | Organizations, departments, cost centers, employee profiles |
| V3 | `V3__travel_domain.sql` | Policies, travel requests, approvals |
| V4 | `V4__booking_and_trip.sql` | Bookings, flights, hotels, itineraries |
| V5 | `V5__finance_and_operations.sql` | Expenses, payments, vendors, audit, notifications |

---

## Indexes (Key)

```sql
CREATE INDEX idx_users_org ON users(organization_id);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_travel_requests_org ON travel_requests(organization_id);
CREATE INDEX idx_travel_requests_status ON travel_requests(status);
CREATE INDEX idx_bookings_request ON bookings(travel_request_id);
CREATE INDEX idx_expense_reports_user ON expense_reports(user_id);
CREATE INDEX idx_audit_logs_entity ON audit_logs(entity_type, entity_id);
```

---

## Seed Data

`SeedDataService` (CommandLineRunner) seeds demo data when `users` table is empty:
- 1 organization (Acme Global Technologies)
- 9 role users with password `password123`
- Sample travel request, booking, expenses

`PermissionSeedService` seeds permissions and role-permission mappings.
