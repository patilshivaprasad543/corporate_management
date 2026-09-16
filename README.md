# CorporateTravel360 — Enterprise Business Travel & Expense Platform

**CorporateTravel360** is a production-style enterprise application for the complete corporate travel lifecycle: planning, policy evaluation, approvals, booking, duty of care, expenses, reimbursement and executive analytics.

## Key Modules

1. Organization, employee and cost-center hierarchy.
2. Travel search and corporate booking workflow.
3. Travel requests, policy validation and multi-tier approvals.
4. Booking, PNR/e-ticket and itinerary management.
5. Active-trip duty of care and traveler support.
6. Expense reports, receipt processing and reimbursement.
7. Finance controls, audit trail and executive analytics.
8. Role-based security with JWT authentication.

## AI Capabilities

AI is an **assistive layer**, not an authorization layer. Core policy, approval, booking and reimbursement decisions remain deterministic and under human/company control.

- **AI Travel Assistant** — natural-language trip planning and policy guidance; actual availability is obtained from configured travel providers.
- **AI Trip Optimizer** — suggests cost, schedule and supplier optimization opportunities.
- **AI Expense Review** — identifies possible expense anomalies or missing information and gives Finance-review guidance.
- **Receipt/expense assistance** — existing receipt-processing flow can surface AI review flags.
- **Optional OpenAI integration** — disabled by default and enabled only through environment configuration.

AI must not invent prices, availability, policy limits, approvals, reimbursements or fraud determinations.

## Technology Stack

- Backend: Java 17, Spring Boot 3.3.4, Spring Security 6.x, Spring Data JPA/Hibernate, WebSocket, OpenAPI/Swagger.
- Frontend: HTML5, Tailwind CSS, Lucide, Chart.js and Vanilla ES6+.
- Database: MySQL 8 / H2.
- DevOps: Docker / Docker Compose.

## AI Configuration

```properties
app.ai.openai.enabled=${AI_ENABLED:false}
app.ai.openai.api-key=${OPENAI_API_KEY:}
app.ai.openai.model=${OPENAI_MODEL:gpt-5.6-luna}
app.ai.openai.base-url=${OPENAI_BASE_URL:https://api.openai.com/v1}
```

Never commit an API key to the repository.

## AI API Catalog

- `POST /api/ai/travel-assistant` — natural-language corporate travel assistant.
- `GET /api/ai/trip-optimizer` — trip optimization guidance.
- `POST /api/ai/expense-review` — expense anomaly/review assistance.
- Swagger: `/swagger-ui/index.html`.

## Quick Start

```bash
./gradlew.bat compileJava test bootJar
java -jar build/libs/corporate_management-1.0.0.jar
```

Open `http://localhost:8080`.

## Verification

```bash
./gradlew.bat test
```

The test suite covers authentication, travel workflow, policy evaluation, analytics, AI endpoints and expense-review logic.
