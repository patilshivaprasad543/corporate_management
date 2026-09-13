CREATE TABLE IF NOT EXISTS policy_violations (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    organization_id     BIGINT NOT NULL,
    policy_id           BIGINT,
    travel_request_id   BIGINT,
    user_id             BIGINT,
    violation_type      VARCHAR(50) NOT NULL,
    rule_name           VARCHAR(150),
    requested_amount    DECIMAL(12,2),
    allowed_amount      DECIMAL(12,2),
    difference_amount   DECIMAL(12,2),
    currency_code       VARCHAR(10) DEFAULT 'INR',
    severity            VARCHAR(20) DEFAULT 'VIOLATION',
    explanation         VARCHAR(1000),
    is_resolved         BOOLEAN DEFAULT FALSE,
    created_at          DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at          DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    is_deleted          BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_pv_org FOREIGN KEY (organization_id) REFERENCES organizations(id),
    CONSTRAINT fk_pv_policy FOREIGN KEY (policy_id) REFERENCES travel_policies(id),
    CONSTRAINT fk_pv_request FOREIGN KEY (travel_request_id) REFERENCES travel_requests(id),
    CONSTRAINT fk_pv_user FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE travel_policies
    ADD COLUMN hotel_room_limit INT DEFAULT 1,
    ADD COLUMN max_transport_amount DECIMAL(12,2) DEFAULT 5000.00,
    ADD COLUMN international_requires_finance BOOLEAN DEFAULT TRUE;
