# CorporateTravel360 — Enterprise Business Travel & Expense Platform

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg)]()
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-blue.svg)]()
[![Java](https://img.shields.io/badge/Java-17%2B-orange.svg)]()
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)]()
[![Security](https://img.shields.io/badge/Spring%20Security-6.x%20JWT-red.svg)]()
[![License](https://img.shields.io/badge/License-Proprietary%20Enterprise-indigo.svg)]()

**CorporateTravel360** is a complete production-style enterprise web application designed to manage the entire lifecycle of corporate business travel from initial planning, automated policy evaluation, multi-tier approvals, booking & e-ticketing, real-time duty of care safety, AI receipt OCR expense filing, finance reimbursement, and executive BI analytics.

---

## Key Modules & Capabilities

1. **Organization & Cost Center Hierarchy**: Multi-tier organization model (`Organization` → `Department` → `Cost Center` → `Project` → `Employee`).
2. **Interactive 8-Phase Lifecycle Flow**:
   - `Phase 1`: Plan & AI Travel Assistant (Search Flights, Hotels, Cabs)
   - `Phase 2`: Travel Request Submission (Automated policy check & budget validation)
   - `Phase 3`: Multi-Tier Manager Approval (Configurable spend thresholds)
   - `Phase 4`: Booking & E-Ticket Issuance (PNR, E-Ticket, Boarding Pass)
   - `Phase 5`: Active Trip & Duty of Care (Live GPS/flight tracker, emergency SOS)
   - `Phase 6`: AI OCR Receipt Scanning & Expense Claim (Automated extraction of merchant and tax)
   - `Phase 7`: Finance Audit & Instant Wallet Settlement (Reimbursement payout)
   - `Phase 8`: Executive BI Analytics & ESG (Carbon emissions saved, spend ROI)
3. **Pluggable Provider Architecture**:
   - `FlightProvider` (Multi-GDS Amadeus / Sabre / Mock)
   - `HotelProvider` (Corporate Negotiated Rates / Mock)
   - `ReceiptOcrProvider` (Vision AI OCR Engine / Mock)
   - `ExchangeRateProvider` (Live Forex Multi-Currency)
4. **Real-Time WebSockets**: Live flight advisories and 24/7 traveler care chat (`/ws-travel`).
5. **Security & Immutable Audit Logging**: BCrypt password encryption, stateless JWT filter chain, IP tracking, and full RBAC enforcement.

---

## Technology Stack

- **Backend**: Java 17, Spring Boot 3.3.4, Spring Security 6.x, Spring Data JPA, Hibernate, SockJS/Stomp WebSocket, OpenAPI / Swagger.
- **Frontend**: Enterprise Single-Page Application (HTML5, Tailwind CSS, Lucide Icons, Chart.js, Vanilla ES6+ Modular Components).
- **Database**: MySQL 8.0 / H2 in-memory mode.
- **DevOps**: Docker, Docker Compose (`docker-compose.yml`), Multi-Stage Build.

---

## Seed Test Credentials

| Role | Username / Email | Password |
|---|---|---|
| **Super Admin** | `superadmin@corporatetravel.com` | `password123` |
| **Corporate Admin** | `admin@acmetech.com` | `password123` |
| **Travel Manager** | `travelmgr@acmetech.com` | `password123` |
| **Line Manager / Approver** | `manager@acmetech.com` | `password123` |
| **Employee / Traveler** | `traveler@acmetech.com` | `password123` |
| **Finance Controller** | `finance@acmetech.com` | `password123` |
| **HR Administrator** | `hr@acmetech.com` | `password123` |
| **Airline Partner / Vendor** | `partner@indigoair.com` | `password123` |
| **24/7 Support Agent** | `support@corporatetravel.com` | `password123` |

---

## Quick Start & Local Execution

### 1. Build and Run via Gradle
```bash
./gradlew.bat compileJava test bootJar
java -jar build/libs/corporate_management-1.0.0.jar
```
Open **`http://localhost:8080`** in your browser.

### 2. Run via Docker Compose
```bash
docker-compose up --build -d
```

---

## API Catalog & Documentation

Interactive Swagger API documentation is available at:
- **`http://localhost:8080/swagger-ui/index.html`**
- OpenAPI 3.0 Specs: `http://localhost:8080/v3/api-docs`

---

## Verification & Automated Testing

Run the full end-to-end automated verification suite:
```bash
python scratch/verify_all_modules.py
```
Outputs 18/18 modules passing with 100% success rate.
