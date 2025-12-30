--liquibase formatted sql

--changeset ${AUTHOR_NAME}:1.6_create_payments_table
CREATE TABLE IF NOT EXISTS  payments (
    payment_id          INT AUTO_INCREMENT PRIMARY KEY,
    transport_id        INT            NOT NULL,
    client_id           INT            NOT NULL,
    amount              DECIMAL(10, 2) NOT NULL,
    payment_datetime    TIMESTAMP,
    status              ENUM('paid', 'unpaid', 'partial') DEFAULT 'unpaid',
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                                    ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (transport_id) REFERENCES transports (transport_id),
    FOREIGN KEY (client_id) REFERENCES clients (client_id),
    INDEX        idx_payment_status (status),
    INDEX        idx_payment_client (client_id)
);