-- CorporateTravel360 Travel Hub migration (MySQL 8+)
-- Apply after the existing database/schema.sql. Safe to run with IF NOT EXISTS.

CREATE TABLE IF NOT EXISTS travel_budgets (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  travel_request_id BIGINT NOT NULL UNIQUE,
  allocated_amount DECIMAL(12,2) NOT NULL DEFAULT 0.00,
  flight_limit DECIMAL(12,2) DEFAULT 0.00,
  hotel_limit DECIMAL(12,2) DEFAULT 0.00,
  cab_limit DECIMAL(12,2) DEFAULT 0.00,
  meal_limit DECIMAL(12,2) DEFAULT 0.00,
  other_limit DECIMAL(12,2) DEFAULT 0.00,
  status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
  allocated_by_user_id BIGINT,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (travel_request_id) REFERENCES travel_requests(id) ON DELETE CASCADE,
  FOREIGN KEY (allocated_by_user_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS travel_agent_contacts (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  travel_request_id BIGINT NOT NULL,
  agency_name VARCHAR(200) NOT NULL,
  agent_name VARCHAR(150) NOT NULL,
  phone_number VARCHAR(40) NOT NULL,
  email VARCHAR(200),
  emergency_phone VARCHAR(40),
  notes VARCHAR(500),
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (travel_request_id) REFERENCES travel_requests(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS travel_documents (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  travel_request_id BIGINT NOT NULL,
  booking_id BIGINT,
  document_type VARCHAR(30) NOT NULL,
  file_name VARCHAR(255) NOT NULL,
  storage_key VARCHAR(500) NOT NULL,
  description VARCHAR(1000),
  visible_to_employee BOOLEAN NOT NULL DEFAULT TRUE,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (travel_request_id) REFERENCES travel_requests(id) ON DELETE CASCADE,
  FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS travel_chat_messages (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  travel_request_id BIGINT NOT NULL,
  sender_user_id BIGINT NOT NULL,
  message VARCHAR(4000) NOT NULL,
  attachment_name VARCHAR(255),
  attachment_key VARCHAR(500),
  system_message BOOLEAN NOT NULL DEFAULT FALSE,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (travel_request_id) REFERENCES travel_requests(id) ON DELETE CASCADE,
  FOREIGN KEY (sender_user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS finance_releases (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  travel_request_id BIGINT NOT NULL,
  requested_by_user_id BIGINT NOT NULL,
  released_by_user_id BIGINT,
  requested_amount DECIMAL(12,2) NOT NULL,
  released_amount DECIMAL(12,2) DEFAULT 0.00,
  payment_reference VARCHAR(100),
  remarks VARCHAR(500),
  status VARCHAR(30) NOT NULL DEFAULT 'REQUESTED',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (travel_request_id) REFERENCES travel_requests(id) ON DELETE CASCADE,
  FOREIGN KEY (requested_by_user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (released_by_user_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_travel_chat_request_created ON travel_chat_messages(travel_request_id, created_at);
CREATE INDEX idx_travel_documents_request ON travel_documents(travel_request_id);
CREATE INDEX idx_finance_releases_request ON finance_releases(travel_request_id, created_at);
