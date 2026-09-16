# CorporateTravel360 — Corporate Travel & Expense Management Platform

Enterprise web application for managing the complete corporate travel and employee expense lifecycle.

## Scope
Employee → Travel Request → Policy Check → Manager Approval → Finance/Admin Approval → Booking → Trip → Expense Claim → Receipt Verification → Finance Audit → Reimbursement → Reports & Audit.

## Core Modules
- Employee and organization management
- Departments, designations and cost centers
- Corporate travel requests
- Travel policy and budget controls
- Multi-level approvals
- Flight, hotel, train and cab bookings
- Itineraries and trip management
- Expense claims and receipts
- Corporate cards and card transactions
- Vendor management
- Finance, payments and reimbursements
- Notifications
- Reports and management dashboards
- Role-based access control
- Audit logging

## Technology
- Java 17
- Spring Boot 3.3.4
- Spring Security 6 / JWT
- Spring Data JPA / Hibernate
- MySQL 8 / H2 for local testing
- Gradle
- HTML5, Tailwind CSS, JavaScript, Chart.js and Lucide
- OpenAPI / Swagger
- WebSocket for supported real-time notifications

## Design Principle
This project is intentionally **non-AI**. Business rules, policy checks, approvals, booking workflows and finance controls are deterministic enterprise workflows. External travel providers can be plugged in behind provider interfaces without coupling the core domain to a vendor.

## Local Configuration
Set database credentials through environment variables:

```text
DB_URL=jdbc:mysql://localhost:3306/corporate_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DB_USERNAME=root
DB_PASSWORD=your_password
JWT_SECRET=replace-with-a-secure-secret
```

Run:

```bash
./gradlew.bat clean test bootJar
java -jar build/libs/corporate_management-1.0.0.jar
```

Swagger: `http://localhost:8080/swagger-ui/index.html`

## Security
Never commit real database passwords, JWT secrets, API keys, production credentials or personal data. Use environment variables or a deployment secret manager.
