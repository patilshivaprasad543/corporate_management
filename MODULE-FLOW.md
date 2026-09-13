# MODULE-FLOW.md — CBTMS Business Workflow Dependencies

## Master Workflow (Target State)

```
SUPER ADMIN
  → Company → Department → Cost Center → Budget
  → Travel Policy → Users/Roles
  → Employee Registration → OTP → Login
  → Travel Request (DRAFT)
  → Policy Evaluation → Submit
  → Approval Engine (Manager → Travel Admin → Finance)
  → Booking Authorization
  → Flight / Hotel / Transport Search & Book
  → Trip → Itinerary → Documents → Risk
  → Travel (IN_PROGRESS)
  → Expense → Receipt → Approval
  → Reimbursement → Payment → Refund
  → Notification → Audit → Analytics → Reports
  → Trip COMPLETED
```

---

## Module Dependency Graph

```mermaid
flowchart TD
    AUTH[Phase 3: Auth + RBAC] --> COMPANY[Phase 4: Company Structure]
    COMPANY --> POLICY[Phase 5: Travel Policy]
    COMPANY --> EMPLOYEE[Phase 4: Employee Profile]
    POLICY --> TREQ[Phase 6: Travel Request]
    EMPLOYEE --> TREQ
    TREQ --> APPROVAL[Phase 7: Approval Engine]
    APPROVAL --> FLIGHT[Phase 8: Flight]
    APPROVAL --> HOTEL[Phase 9: Hotel]
    APPROVAL --> TRANSPORT[Phase 10: Transport]
    FLIGHT --> BOOKING[Phase 11: Booking]
    HOTEL --> BOOKING
    TRANSPORT --> BOOKING
    BOOKING --> TRIP[Phase 12: Trip + Itinerary]
    TRIP --> DOCS[Phase 13: Documents]
    TRIP --> RISK[Phase 14: Risk]
    TRIP --> EXPENSE[Phase 15: Expenses]
    EXPENSE --> REIMB[Phase 16: Reimbursement/Payment]
    REIMB --> NOTIFY[Phase 19: Notifications]
    NOTIFY --> AUDIT[Audit - cross-cutting]
    AUDIT --> ANALYTICS[Phase 23: Analytics/Reports]
    VENDOR[Phase 17: Vendor] --> BOOKING
    AGENT[Phase 18: Travel Agent] --> BOOKING
    SUPPORT[Phase 20: Support] --> NOTIFY
    AI[Phase 21: AI Assistant] -.-> TREQ
    AI -.-> EXPENSE
    SUSTAIN[Phase 22: Sustainability] --> ANALYTICS
```

---

## Current Implementation vs Target

| Stage | Backend | Frontend | DB | Connected |
|-------|---------|----------|-----|-----------|
| Auth + Portals | ✅ | ✅ Shell | ✅ | Partial |
| Company CRUD | Entity only | ❌ | ✅ org table | ❌ |
| Employee Profile | Entity + seed | ❌ | ✅ | ❌ |
| Travel Policy | Service | ❌ | ✅ | Partial |
| Travel Request | Service + API | Legacy SPA | ✅ | Partial |
| Approval | Service | Legacy SPA | ✅ | Partial |
| Booking | Service + API | Legacy SPA | ✅ | Partial |
| Trip/Itinerary | Entities | ❌ | ✅ | ❌ Not auto-created |
| Expense | Service + API | Legacy SPA | ✅ | Partial |
| Reimbursement | ❌ | ❌ | ❌ | ❌ |
| Payment/Refund | Partial entity | ❌ | Partial | ❌ |
| Notifications | Service | ❌ | ✅ | Partial |
| Audit | Service | ❌ | ✅ | Partial |
| Analytics | Mock aggregates | ❌ | Partial | ❌ |

---

## Cross-Cutting Concerns (Every Module)

1. **CompanyContext** — filter all queries by `organization_id`
2. **Permission check** — `@PreAuthorize` or service-level
3. **AuditLog** — on every state change
4. **Notification** — on business events
5. **Optimistic locking** — on concurrent updates

---

## API Flow Example: Travel Request → Booking

```
POST /api/travel-requests          → TravelRequestService.create (DRAFT)
POST /api/travel-requests/{id}/evaluate → PolicyEvaluationService
POST /api/travel-requests/{id}/submit   → StateMachine → SUBMITTED
GET  /api/approvals/pending             → ApprovalWorkflowService
POST /api/approvals/{id}/approve        → StateMachine → MANAGER_APPROVED
POST /api/flights/search                → FlightProvider.search
POST /api/bookings                      → BookingService → TripService.create
```

Each arrow must persist FK relationships and emit audit + notification events.

---

## Frontend Portal → Module Access

| Portal | Primary Modules |
|--------|----------------|
| Employee | Travel Requests, Bookings, Trips, Expenses, Documents |
| Manager | Approvals, Team Travel, Team Expenses |
| Travel Admin | Policies, Bookings, Vendors, Risk |
| Finance | Expenses, Reimbursements, Payments, Reports |
| Travel Agent | Assigned Requests, Bookings, Modifications |
| Vendor | Bookings, Invoices, Services |
| Super Admin | Companies, Users, System Config, Audit |
