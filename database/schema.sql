-- =========================================================================
-- CORPORATETRAVEL360 — PRODUCTION DATABASE SCHEMA DDL SCRIPT
-- Compatible with MySQL 8.0+
-- =========================================================================

CREATE DATABASE IF NOT EXISTS `corporate_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `corporate_db`;

-- 1. Organizations & Companies
CREATE TABLE IF NOT EXISTS `organizations` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `code` VARCHAR(50) UNIQUE NOT NULL,
    `domain` VARCHAR(150),
    `subscription_tier` VARCHAR(50) DEFAULT 'ENTERPRISE',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. Departments
CREATE TABLE IF NOT EXISTS `departments` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `organization_id` BIGINT NOT NULL,
    `name` VARCHAR(150) NOT NULL,
    `code` VARCHAR(50) NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`organization_id`) REFERENCES `organizations`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. Cost Centers
CREATE TABLE IF NOT EXISTS `cost_centers` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `organization_id` BIGINT NOT NULL,
    `department_id` BIGINT,
    `code` VARCHAR(50) UNIQUE NOT NULL,
    `name` VARCHAR(150) NOT NULL,
    `allocated_budget` DECIMAL(15, 2) DEFAULT 0.00,
    `utilized_budget` DECIMAL(15, 2) DEFAULT 0.00,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`organization_id`) REFERENCES `organizations`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`department_id`) REFERENCES `departments`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. Users & Credentials
CREATE TABLE IF NOT EXISTS `users` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `organization_id` BIGINT,
    `department_id` BIGINT,
    `username` VARCHAR(100) UNIQUE NOT NULL,
    `email` VARCHAR(150) UNIQUE NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `full_name` VARCHAR(150) NOT NULL,
    `phone_number` VARCHAR(30),
    `employee_code` VARCHAR(50) UNIQUE,
    `designation` VARCHAR(100),
    `status` VARCHAR(30) DEFAULT 'ACTIVE',
    `manager_id` BIGINT,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`organization_id`) REFERENCES `organizations`(`id`) ON DELETE SET NULL,
    FOREIGN KEY (`department_id`) REFERENCES `departments`(`id`) ON DELETE SET NULL,
    FOREIGN KEY (`manager_id`) REFERENCES `users`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. User Roles (RBAC)
CREATE TABLE IF NOT EXISTS `user_roles` (
    `user_id` BIGINT NOT NULL,
    `role` VARCHAR(50) NOT NULL,
    PRIMARY KEY (`user_id`, `role`),
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6. Travel Policies & Rules
CREATE TABLE IF NOT EXISTS `travel_policies` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `organization_id` BIGINT NOT NULL,
    `name` VARCHAR(150) NOT NULL,
    `policy_tier` VARCHAR(50) DEFAULT 'TIER_1',
    `max_domestic_flight_amount` DECIMAL(12, 2) DEFAULT 15000.00,
    `max_international_flight_amount` DECIMAL(12, 2) DEFAULT 75000.00,
    `max_hotel_per_night` DECIMAL(12, 2) DEFAULT 6000.00,
    `daily_meal_per_diem` DECIMAL(12, 2) DEFAULT 2000.00,
    `advance_booking_days` INT DEFAULT 7,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`organization_id`) REFERENCES `organizations`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 7. Travel Requests
CREATE TABLE IF NOT EXISTS `travel_requests` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `request_number` VARCHAR(50) UNIQUE NOT NULL,
    `trip_name` VARCHAR(255) NOT NULL,
    `trip_type` VARCHAR(50) NOT NULL,
    `origin` VARCHAR(100) NOT NULL,
    `destination` VARCHAR(100) NOT NULL,
    `departure_date` DATE NOT NULL,
    `return_date` DATE,
    `estimated_budget` DECIMAL(12, 2) NOT NULL,
    `status` VARCHAR(50) NOT NULL DEFAULT 'SUBMITTED',
    `policy_compliance_status` VARCHAR(50) DEFAULT 'COMPLIANT',
    `business_justification` TEXT,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 8. Approval Steps & Hierarchy
CREATE TABLE IF NOT EXISTS `approval_steps` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `travel_request_id` BIGINT NOT NULL,
    `approver_id` BIGINT NOT NULL,
    `step_order` INT NOT NULL DEFAULT 1,
    `status` VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    `action_timestamp` DATETIME,
    `comments` VARCHAR(500),
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`travel_request_id`) REFERENCES `travel_requests`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`approver_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 9. Bookings & Itineraries
CREATE TABLE IF NOT EXISTS `bookings` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `travel_request_id` BIGINT,
    `booking_reference` VARCHAR(50) UNIQUE NOT NULL,
    `pnr_number` VARCHAR(30),
    `booking_type` VARCHAR(50) NOT NULL,
    `status` VARCHAR(50) NOT NULL DEFAULT 'CONFIRMED',
    `total_amount` DECIMAL(12, 2) NOT NULL,
    `carbon_kg` DECIMAL(8, 2) DEFAULT 0.00,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`travel_request_id`) REFERENCES `travel_requests`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 10. Expenses & Receipts
CREATE TABLE IF NOT EXISTS `expense_reports` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `travel_request_id` BIGINT,
    `report_number` VARCHAR(50) UNIQUE NOT NULL,
    `title` VARCHAR(255) NOT NULL,
    `total_amount` DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    `status` VARCHAR(50) NOT NULL DEFAULT 'SUBMITTED',
    `ocr_verified` BOOLEAN DEFAULT FALSE,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`travel_request_id`) REFERENCES `travel_requests`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 11. Security Audit Logs
CREATE TABLE IF NOT EXISTS `audit_logs` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT,
    `action` VARCHAR(100) NOT NULL,
    `entity_type` VARCHAR(100) NOT NULL,
    `entity_id` BIGINT,
    `details` TEXT,
    `ip_address` VARCHAR(50),
    `status` VARCHAR(30) DEFAULT 'SUCCESS',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
