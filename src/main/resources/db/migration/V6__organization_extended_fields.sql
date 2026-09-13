ALTER TABLE organizations
    ADD COLUMN registration_number VARCHAR(100),
    ADD COLUMN tax_number VARCHAR(100),
    ADD COLUMN email VARCHAR(150),
    ADD COLUMN phone VARCHAR(50),
    ADD COLUMN timezone VARCHAR(50) DEFAULT 'UTC';
