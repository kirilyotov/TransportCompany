--liquibase formatted sql

--changeset ${AUTHOR_NAME}:1.2_create_vehicles_table
CREATE TABLE IF NOT EXISTS  vehicles (
    vehicle_id INT AUTO_INCREMENT PRIMARY KEY,
    license_plate VARCHAR(20) NOT NULL UNIQUE,
    type ENUM('bus', 'truck', 'tanker', 'other') NOT NULL,
    capacity_weight DECIMAL(8,2),
    capacity_passengers INT,
    company_id INT NOT NULL,
    status ENUM('active', 'in_repair', 'inactive') DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (company_id) REFERENCES companies(company_id) ON DELETE CASCADE,
    INDEX idx_vehicle_company (company_id),
    INDEX idx_vehicle_type (type),
    INDEX idx_vehicle_status (status)
);