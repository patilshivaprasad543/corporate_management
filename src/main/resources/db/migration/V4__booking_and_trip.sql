-- V4: Bookings, flights, hotels, transport, itineraries

CREATE TABLE IF NOT EXISTS bookings (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_reference   VARCHAR(50) NOT NULL UNIQUE,
    pnr_number          VARCHAR(30),
    booking_type        VARCHAR(30) NOT NULL,
    status              VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    total_amount        DECIMAL(12,2),
    currency_code       VARCHAR(10) DEFAULT 'INR',
    carbon_kg           DECIMAL(10,2),
    travel_request_id   BIGINT,
    user_id             BIGINT NOT NULL,
    organization_id     BIGINT,
    created_at          DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at          DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    is_deleted          BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_booking_tr FOREIGN KEY (travel_request_id) REFERENCES travel_requests(id),
    CONSTRAINT fk_booking_user FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS booking_items (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id      BIGINT NOT NULL,
    item_type       VARCHAR(30),
    description     VARCHAR(500),
    amount          DECIMAL(12,2),
    created_at      DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at      DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    is_deleted      BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_bi_booking FOREIGN KEY (booking_id) REFERENCES bookings(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS flights (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    flight_number   VARCHAR(20),
    airline         VARCHAR(100),
    origin_code     VARCHAR(10),
    destination_code VARCHAR(10),
    departure_time  DATETIME(6),
    arrival_time    DATETIME(6),
    price           DECIMAL(12,2),
    travel_class    VARCHAR(30),
    created_at      DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at      DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    is_deleted      BOOLEAN DEFAULT FALSE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS hotels (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(200),
    city            VARCHAR(100),
    rating          DECIMAL(3,1),
    price_per_night DECIMAL(12,2),
    category        VARCHAR(30),
    created_at      DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at      DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    is_deleted      BOOLEAN DEFAULT FALSE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS transportation (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    service_type    VARCHAR(50),
    provider        VARCHAR(100),
    origin          VARCHAR(100),
    destination     VARCHAR(100),
    price           DECIMAL(12,2),
    created_at      DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at      DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    is_deleted      BOOLEAN DEFAULT FALSE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS itineraries (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    trip_name           VARCHAR(200),
    travel_request_id   BIGINT,
    user_id             BIGINT,
    start_date          DATE,
    end_date            DATE,
    status              VARCHAR(30),
    created_at          DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at          DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    is_deleted          BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_itin_tr FOREIGN KEY (travel_request_id) REFERENCES travel_requests(id),
    CONSTRAINT fk_itin_user FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS itinerary_events (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    itinerary_id    BIGINT NOT NULL,
    event_type      VARCHAR(50),
    title           VARCHAR(200),
    description     VARCHAR(1000),
    start_time      DATETIME(6),
    end_time        DATETIME(6),
    location        VARCHAR(200),
    created_at      DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at      DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    is_deleted      BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_event_itin FOREIGN KEY (itinerary_id) REFERENCES itineraries(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
