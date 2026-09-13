-- V2: Departments, cost centers, employee profiles

CREATE TABLE IF NOT EXISTS departments (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(150) NOT NULL,
    code            VARCHAR(50),
    organization_id BIGINT NOT NULL,
    manager_id      BIGINT,
    travel_budget   DECIMAL(15,2),
    allocated_budget DECIMAL(15,2),
    spent_budget    DECIMAL(15,2),
    created_at      DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at      DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    is_deleted      BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_dept_org FOREIGN KEY (organization_id) REFERENCES organizations(id),
    CONSTRAINT fk_dept_manager FOREIGN KEY (manager_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS cost_centers (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(150) NOT NULL,
    code            VARCHAR(50),
    organization_id BIGINT NOT NULL,
    department_id   BIGINT,
    budget_limit    DECIMAL(15,2),
    current_spend   DECIMAL(15,2),
    created_at      DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at      DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    is_deleted      BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_cc_org FOREIGN KEY (organization_id) REFERENCES organizations(id),
    CONSTRAINT fk_cc_dept FOREIGN KEY (department_id) REFERENCES departments(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS employee_profiles (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id                 BIGINT NOT NULL UNIQUE,
    employee_code           VARCHAR(50),
    designation             VARCHAR(100),
    department_id           BIGINT,
    cost_center_id          BIGINT,
    manager_id              BIGINT,
    allowed_travel_class    VARCHAR(30),
    allowed_hotel_category  VARCHAR(30),
    passport_number         VARCHAR(50),
    passport_expiry         DATE,
    frequent_flyer_number   VARCHAR(50),
    preferred_airline       VARCHAR(100),
    preferred_hotel_chain   VARCHAR(100),
    emergency_contact_name  VARCHAR(100),
    emergency_contact_phone VARCHAR(30),
    created_at              DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at              DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    is_deleted              BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_emp_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_emp_dept FOREIGN KEY (department_id) REFERENCES departments(id),
    CONSTRAINT fk_emp_cc FOREIGN KEY (cost_center_id) REFERENCES cost_centers(id),
    CONSTRAINT fk_emp_mgr FOREIGN KEY (manager_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
