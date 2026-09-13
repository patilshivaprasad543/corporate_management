-- V3: Travel policies, requests, approvals

CREATE TABLE IF NOT EXISTS travel_policies (
    id                          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name                        VARCHAR(200) NOT NULL,
    description                 VARCHAR(1000),
    organization_id             BIGINT NOT NULL,
    max_domestic_flight_price   DECIMAL(12,2),
    max_international_flight_price DECIMAL(12,2),
    max_hotel_price_per_night   DECIMAL(12,2),
    daily_meal_allowance        DECIMAL(12,2),
    daily_taxi_allowance        DECIMAL(12,2),
    advance_booking_days        INT,
    allowed_flight_class        VARCHAR(30),
    allowed_hotel_category      VARCHAR(30),
    finance_approval_threshold  DECIMAL(12,2),
    admin_approval_threshold    DECIMAL(12,2),
    require_manager_approval    BOOLEAN DEFAULT TRUE,
    is_active                   BOOLEAN DEFAULT TRUE,
    created_at                  DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at                  DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    is_deleted                  BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_policy_org FOREIGN KEY (organization_id) REFERENCES organizations(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS policy_rules (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    policy_id       BIGINT NOT NULL,
    rule_type       VARCHAR(50),
    rule_value      VARCHAR(255),
    description     VARCHAR(500),
    created_at      DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at      DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    is_deleted      BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_rule_policy FOREIGN KEY (policy_id) REFERENCES travel_policies(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS travel_requests (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    request_number          VARCHAR(50) NOT NULL UNIQUE,
    trip_name               VARCHAR(200) NOT NULL,
    trip_type               VARCHAR(30) NOT NULL,
    origin                  VARCHAR(100) NOT NULL,
    destination             VARCHAR(100) NOT NULL,
    departure_date          DATE NOT NULL,
    return_date             DATE,
    is_round_trip           BOOLEAN DEFAULT TRUE,
    is_personal_trip        BOOLEAN DEFAULT FALSE,
    number_of_travelers     INT DEFAULT 1,
    business_justification  VARCHAR(1000),
    client_or_event_name    VARCHAR(200),
    estimated_budget        DECIMAL(12,2),
    preferred_travel_class    VARCHAR(30),
    preferred_hotel_category VARCHAR(30),
    status                  VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    compliance_status       VARCHAR(30),
    policy_violation_reason VARCHAR(1000),
    employee_id             BIGINT NOT NULL,
    department_id           BIGINT,
    cost_center_id          BIGINT,
    organization_id         BIGINT NOT NULL,
    created_at              DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at              DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    is_deleted              BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_tr_employee FOREIGN KEY (employee_id) REFERENCES users(id),
    CONSTRAINT fk_tr_dept FOREIGN KEY (department_id) REFERENCES departments(id),
    CONSTRAINT fk_tr_cc FOREIGN KEY (cost_center_id) REFERENCES cost_centers(id),
    CONSTRAINT fk_tr_org FOREIGN KEY (organization_id) REFERENCES organizations(id),
    INDEX idx_tr_org (organization_id),
    INDEX idx_tr_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS approval_steps (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    travel_request_id   BIGINT NOT NULL,
    step_order          INT NOT NULL,
    approver_role       VARCHAR(50),
    approver_id         BIGINT,
    status              VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    comments            VARCHAR(1000),
    action_timestamp    DATETIME(6),
    created_at          DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at          DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    is_deleted          BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_approval_tr FOREIGN KEY (travel_request_id) REFERENCES travel_requests(id),
    CONSTRAINT fk_approval_user FOREIGN KEY (approver_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
