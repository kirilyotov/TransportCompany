--liquibase formatted sql

--changeset ${AUTHOR_NAME}:1.4_create_transports_table
CREATE TABLE IF NOT EXISTS transports (
    transport_id INT AUTO_INCREMENT PRIMARY KEY,
    start_point VARCHAR(100) NOT NULL,
    end_point VARCHAR(100) NOT NULL,
    departure_date DATETIME NOT NULL,
    arrival_date DATETIME,
    vehicle_id INT NOT NULL,
    driver_id INT NOT NULL,
    client_id INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    company_id INT NOT NULL,
    status ENUM('planned', 'in_progress', 'completed', 'cancelled') DEFAULT 'planned',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id),
    FOREIGN KEY (driver_id) REFERENCES employees(employee_id),
    FOREIGN KEY (client_id) REFERENCES clients(client_id),
    FOREIGN KEY (company_id) REFERENCES companies(company_id),
    INDEX idx_transport_destination (end_point),
    INDEX idx_transport_dates (departure_date, arrival_date),
    INDEX idx_transport_company (company_id)
);