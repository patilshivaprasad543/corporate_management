-- V5: Expenses, payments, vendors, notifications, audit, chat, risk, carbon

CREATE TABLE IF NOT EXISTS expense_reports (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    report_number       VARCHAR(50) NOT NULL UNIQUE,
    title               VARCHAR(200),
    total_amount        DECIMAL(12,2) DEFAULT 0,
    currency_code       VARCHAR(10) DEFAULT 'INR',
    status              VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    ocr_verified        BOOLEAN DEFAULT FALSE,
    user_id             BIGINT NOT NULL,
    travel_request_id   BIGINT,
    organization_id     BIGINT,
    created_at          DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at          DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    is_deleted          BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_exp_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_exp_tr FOREIGN KEY (travel_request_id) REFERENCES travel_requests(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS expense_items (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    expense_report_id   BIGINT NOT NULL,
    category            VARCHAR(50),
    description         VARCHAR(500),
    amount              DECIMAL(12,2),
    expense_date        DATE,
    merchant            VARCHAR(200),
    receipt_url         VARCHAR(500),
    created_at          DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at          DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    is_deleted          BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_ei_report FOREIGN KEY (expense_report_id) REFERENCES expense_reports(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS invoices (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    invoice_number  VARCHAR(50) UNIQUE,
    amount          DECIMAL(12,2),
    status          VARCHAR(30),
    vendor_id       BIGINT,
    organization_id BIGINT,
    created_at      DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at      DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    is_deleted      BOOLEAN DEFAULT FALSE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS payment_transactions (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    reference       VARCHAR(100),
    amount          DECIMAL(12,2),
    currency_code   VARCHAR(10) DEFAULT 'INR',
    status          VARCHAR(30),
    payment_method  VARCHAR(30),
    user_id         BIGINT,
    created_at      DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at      DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    is_deleted      BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_pay_user FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS travel_wallets (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id             BIGINT NOT NULL UNIQUE,
    allocated_budget    DECIMAL(15,2),
    approved_budget     DECIMAL(15,2),
    used_budget         DECIMAL(15,2) DEFAULT 0,
    pending_expenses    DECIMAL(15,2) DEFAULT 0,
    reimbursed_amount   DECIMAL(15,2) DEFAULT 0,
    corporate_card_limit DECIMAL(15,2),
    currency_code       VARCHAR(10) DEFAULT 'INR',
    created_at          DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at          DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    is_deleted          BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_wallet_user FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS corporate_cards (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    card_token      VARCHAR(100),
    last_four_digits VARCHAR(4),
    card_holder_name VARCHAR(100),
    card_type       VARCHAR(50),
    expiry_date     DATE,
    spending_limit  DECIMAL(15,2),
    current_balance DECIMAL(15,2),
    is_active       BOOLEAN DEFAULT TRUE,
    user_id         BIGINT,
    organization_id BIGINT,
    created_at      DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at      DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    is_deleted      BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_card_user FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS vendors (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(200) NOT NULL,
    code            VARCHAR(50),
    vendor_type     VARCHAR(50),
    contact_email   VARCHAR(150),
    contact_phone   VARCHAR(30),
    is_active       BOOLEAN DEFAULT TRUE,
    organization_id BIGINT,
    created_at      DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at      DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    is_deleted      BOOLEAN DEFAULT FALSE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS notifications (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT NOT NULL,
    title           VARCHAR(200),
    message         VARCHAR(1000),
    type            VARCHAR(50),
    is_read         BOOLEAN DEFAULT FALSE,
    created_at      DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at      DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    is_deleted      BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_notif_user FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS audit_logs (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_email      VARCHAR(150),
    action_name     VARCHAR(100) NOT NULL,
    entity_type     VARCHAR(100),
    entity_id       BIGINT,
    description     VARCHAR(1000),
    ip_address      VARCHAR(50),
    status          VARCHAR(50) DEFAULT 'SUCCESS',
    created_at      DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at      DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    is_deleted      BOOLEAN DEFAULT FALSE,
    INDEX idx_audit_entity (entity_type, entity_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS risk_alerts (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    destination     VARCHAR(100),
    risk_level      VARCHAR(30),
    alert_message   VARCHAR(1000),
    user_id         BIGINT,
    travel_request_id BIGINT,
    is_active       BOOLEAN DEFAULT TRUE,
    created_at      DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at      DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    is_deleted      BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_risk_user FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS carbon_records (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    source_type     VARCHAR(50),
    carbon_kg       DECIMAL(10,2),
    user_id         BIGINT,
    travel_request_id BIGINT,
    created_at      DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at      DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    is_deleted      BOOLEAN DEFAULT FALSE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS chat_conversations (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT,
    subject         VARCHAR(200),
    status          VARCHAR(30),
    created_at      DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at      DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    is_deleted      BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_chat_user FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS chat_messages (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    conversation_id     BIGINT NOT NULL,
    sender_id           BIGINT,
    message             TEXT,
    is_from_support     BOOLEAN DEFAULT FALSE,
    created_at          DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at          DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    is_deleted          BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_msg_conv FOREIGN KEY (conversation_id) REFERENCES chat_conversations(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
