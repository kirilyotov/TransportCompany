--liquibase formatted sql

--changeset ${AUTHOR_NAME}:1.3_create_employees_table
CREATE TABLE IF NOT EXISTS  employees (
    employee_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    ucn VARCHAR(10) NOT NULL UNIQUE,
    phone VARCHAR(20),
    salary DECIMAL(10,2),
    qualifications JSON,  -- e.g. ["hazardous", "over_12_passengers"]
    company_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (company_id) REFERENCES companies(company_id) ON DELETE CASCADE,
    INDEX idx_employee_company (company_id),
    INDEX idx_employee_salary (salary)
);